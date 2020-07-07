/*
 * EduSupport - Minecraft Educational Support for Geyser
 * Copyright (C) 2020 EduSupport Developers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.com.grieve.edusupport.network.translators.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.inventory.InventorySource;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import lombok.ToString;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.item.ItemTranslator;
import org.geysermc.connector.utils.InventoryUtils;

import java.util.List;

@ToString
public class PlayerInventoryTranslator extends BaseInventoryTranslator {
    private static ItemData UNUSUABLE_CRAFTING_SPACE_BLOCK;

    public PlayerInventoryTranslator() {
        super(46);
    }

    /**
     * Update the crafting grid for the player to hide/show the barriers in the creative inventory
     *
     * @param session   Session of the player
     * @param inventory Inventory of the player
     */
    public static void updateCraftingGrid(GeyserSession session, Inventory inventory) {
        // Crafting grid
        for (int i = 1; i < 5; i++) {
            InventorySlotPacket slotPacket = new InventorySlotPacket();
            slotPacket.setContainerId(ContainerId.UI);
            slotPacket.setSlot(i + 27);

            if (session.getGameMode() == GameMode.CREATIVE) {
                slotPacket.setItem(getUnusuableCraftingSpaceBlock());
            } else {
                slotPacket.setItem(ItemTranslator.translateToBedrock(session, inventory.getItem(i)));
            }

            session.sendUpstreamPacket(slotPacket);
        }
    }

    private static ItemData getUnusuableCraftingSpaceBlock() {
        if (UNUSUABLE_CRAFTING_SPACE_BLOCK == null) {
            UNUSUABLE_CRAFTING_SPACE_BLOCK = InventoryUtils.createUnusableSpaceBlock(
                    "The creative crafting grid is\nunavailable in Java Edition");
        }

        return UNUSUABLE_CRAFTING_SPACE_BLOCK;
    }

    @Override
    public void updateInventory(GeyserSession session, Inventory inventory) {
        updateCraftingGrid(session, inventory);

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.INVENTORY);
        ItemData[] contents = new ItemData[36];
        // Inventory
        for (int i = 9; i < 36; i++) {
            contents[i] = ItemTranslator.translateToBedrock(session, inventory.getItem(i));
        }
        // Hotbar
        for (int i = 36; i < 45; i++) {
            contents[i - 36] = ItemTranslator.translateToBedrock(session, inventory.getItem(i));
        }
        inventoryContentPacket.setContents(contents);
        session.sendUpstreamPacket(inventoryContentPacket);

        // Armor
        InventoryContentPacket armorContentPacket = new InventoryContentPacket();
        armorContentPacket.setContainerId(ContainerId.ARMOR);
        contents = new ItemData[4];
        for (int i = 5; i < 9; i++) {
            contents[i - 5] = ItemTranslator.translateToBedrock(session, inventory.getItem(i));
        }
        armorContentPacket.setContents(contents);
        session.sendUpstreamPacket(armorContentPacket);

        // Offhand
        InventoryContentPacket offhandPacket = new InventoryContentPacket();
        offhandPacket.setContainerId(ContainerId.OFFHAND);
        offhandPacket.setContents(new ItemData[]{ItemTranslator.translateToBedrock(session, inventory.getItem(45))});
        session.sendUpstreamPacket(offhandPacket);
    }

    @Override
    public void updateSlot(GeyserSession session, Inventory inventory, int slot) {
        if (slot >= 1 && slot <= 44) {
            InventorySlotPacket slotPacket = new InventorySlotPacket();
            if (slot >= 9) {
                slotPacket.setContainerId(ContainerId.INVENTORY);
                if (slot >= 36) {
                    slotPacket.setSlot(slot - 36);
                } else {
                    slotPacket.setSlot(slot);
                }
            } else if (slot >= 5) {
                slotPacket.setContainerId(ContainerId.ARMOR);
                slotPacket.setSlot(slot - 5);
            } else {
                slotPacket.setContainerId(ContainerId.UI);
                slotPacket.setSlot(slot + 27);
            }
            slotPacket.setItem(ItemTranslator.translateToBedrock(session, inventory.getItem(slot)));
            session.sendUpstreamPacket(slotPacket);
        } else if (slot == 45) {
            InventoryContentPacket offhandPacket = new InventoryContentPacket();
            offhandPacket.setContainerId(ContainerId.OFFHAND);
            offhandPacket.setContents(new ItemData[]{ItemTranslator.translateToBedrock(session, inventory.getItem(slot))});
            session.sendUpstreamPacket(offhandPacket);
        }
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        int slotnum = action.getSlot();
        switch (action.getSource().getContainerId()) {
            case ContainerId.CRAFTING_ADD_INGREDIENT:
            case ContainerId.DROP_CONTENTS:
                return slotnum + 1;
        }
        return super.bedrockSlotToJava(action);
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        return slot;
    }

    @Override
    public boolean isOutput(InventoryActionData action) {
        return action.getSlot() == 50;
    }

    @Override
    public void prepareInventory(GeyserSession session, Inventory inventory) {
    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {
    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
    }

    @Override
    public void updateProperty(GeyserSession session, Inventory inventory, int key, int value) {
    }

    @Override
    public void translateActions(GeyserSession session, Inventory inventory, List<InventoryActionData> actions) {
        if (session.getGameMode() == GameMode.CREATIVE) {
            //crafting grid is not visible in creative mode in java edition
            for (InventoryActionData action : actions) {
                if (action.getSource().getContainerId() == ContainerId.CRAFTING_ADD_INGREDIENT && (action.getSlot() >= 0 && 5 >= action.getSlot())) {
                    updateInventory(session, inventory);
                    InventoryUtils.updateCursor(session);
                    return;
                }
            }

            ItemStack javaItem;
            for (InventoryActionData action : actions) {
                switch (action.getSource().getContainerId()) {
                    case ContainerId.INVENTORY:
                    case ContainerId.ARMOR:
                    case ContainerId.OFFHAND:
                        int javaSlot = bedrockSlotToJava(action);
                        if (action.getToItem().getId() == 0) {
                            javaItem = new ItemStack(-1, 0, null);
                        } else {
                            javaItem = ItemTranslator.translateToJava(action.getToItem());
                        }
                        ClientCreativeInventoryActionPacket creativePacket = new ClientCreativeInventoryActionPacket(javaSlot, javaItem);
                        session.sendDownstreamPacket(creativePacket);
                        inventory.setItem(javaSlot, javaItem);
                        break;
                    case ContainerId.UI:
                        if (action.getSlot() == 0) {
                            session.getInventory().setCursor(ItemTranslator.translateToJava(action.getToItem()));
                        }
                        break;
                    case ContainerId.NONE:
                        if (action.getSource().getType() == InventorySource.Type.WORLD_INTERACTION
                                && action.getSource().getFlag() == InventorySource.Flag.DROP_ITEM) {
                            javaItem = ItemTranslator.translateToJava(action.getToItem());
                            ClientCreativeInventoryActionPacket creativeDropPacket = new ClientCreativeInventoryActionPacket(-1, javaItem);
                            session.sendDownstreamPacket(creativeDropPacket);
                        }
                        break;
                }
            }
            return;
        }

        // Remove Useless Packet
        if (actions.stream().anyMatch(a -> a.getSource().getContainerId() == ContainerId.CRAFTING_USE_INGREDIENT)) {
            return;
        }

        super.translateActions(session, inventory, actions);
    }
}
