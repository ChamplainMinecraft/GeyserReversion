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

import au.com.grieve.edusupport.network.translators.inventory.action.Execute;
import au.com.grieve.edusupport.network.translators.inventory.action.Transaction;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.MessageSerializer;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientRenameItemPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.updater.ContainerInventoryUpdater;

import java.util.List;
import java.util.stream.Collectors;


public class AnvilInventoryTranslator extends BlockInventoryTranslator {

    public AnvilInventoryTranslator() {
        super(3, "minecraft:anvil[facing=north]", ContainerType.ANVIL, new ContainerInventoryUpdater());
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        int slotnum = action.getSlot();
        switch (action.getSource().getContainerId()) {
            case ContainerId.CONTAINER_INPUT:
            case ContainerId.ANVIL_MATERIAL:
            case ContainerId.DROP_CONTENTS:
                return slotnum;
            case ContainerId.ANVIL_RESULT:
                return 2;
        }
        return super.bedrockSlotToJava(action);
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        if (slot < size) {
            return slot;
        }
        return super.javaSlotToBedrock(slot);
    }

    @Override
    public boolean isOutput(InventoryActionData action) {
        return action.getSource().getContainerId() == ContainerId.ANVIL_RESULT && action.getSlot() == 0;
    }

    @Override
    public void translateActions(GeyserSession session, Inventory inventory, List<InventoryActionData> actions) {
        // If we have an anvil_result then we filter out anvil_material and container_input
        if (actions.stream().anyMatch(this::isOutput)) {
            actions = actions.stream()
                    .filter(a -> a.getSource().getContainerId() != ContainerId.ANVIL_MATERIAL)
                    .filter(a -> a.getSource().getContainerId() != ContainerId.CONTAINER_INPUT)
                    .collect(Collectors.toList());
        }

        super.translateActions(session, inventory, actions);
    }

    @Override
    protected void processAction(Transaction transaction, ActionData cursor, ActionData from, ActionData to) {
        // If from is ANVIL_RESULT we add a rename packet
        if (from.action.getSource().getContainerId() == ContainerId.ANVIL_RESULT) {
            transaction.add(new Execute(() -> {
                ItemData item = from.action.getFromItem();
                NbtMap tag = item.getTag();
                String rename = tag != null ? tag.getCompound("display").getString("Name") : "";
                ClientRenameItemPacket renameItemPacket = new ClientRenameItemPacket(rename);
                transaction.getSession().sendDownstreamPacket(renameItemPacket);
            }));
        }

        super.processAction(transaction, cursor, from, to);
    }

    @Override
    public void updateSlot(GeyserSession session, Inventory inventory, int slot) {
        if (slot >= 0 && slot <= 2) {
            ItemStack item = inventory.getItem(slot);
            if (item != null) {
                String rename;
                CompoundTag tag = item.getNbt();
                if (tag != null) {
                    CompoundTag displayTag = tag.get("display");
                    if (displayTag != null) {
                        String itemName = displayTag.get("Name").getValue().toString();
                        TextMessage message = (TextMessage) MessageSerializer.fromString(itemName);
                        rename = message.getText();
                    } else {
                        rename = "";
                    }
                } else {
                    rename = "";
                }
                ClientRenameItemPacket renameItemPacket = new ClientRenameItemPacket(rename);
                session.sendDownstreamPacket(renameItemPacket);
            }
        }
        super.updateSlot(session, inventory, slot);
    }
}
