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

import au.com.grieve.edusupport.network.translators.inventory.action.Transaction;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.updater.ContainerInventoryUpdater;
import org.geysermc.connector.network.translators.inventory.updater.InventoryUpdater;

import java.util.List;


public class CraftingInventoryTranslator extends BaseInventoryTranslator {

    private final InventoryUpdater updater;

    public CraftingInventoryTranslator() {
        super(10);
        this.updater = new ContainerInventoryUpdater();
    }

    @Override
    public void prepareInventory(GeyserSession session, Inventory inventory) {
        //
    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) inventory.getId());
        containerOpenPacket.setType(ContainerType.WORKBENCH);
        containerOpenPacket.setBlockPosition(inventory.getHolderPosition());
        containerOpenPacket.setUniqueEntityId(inventory.getHolderId());
        session.sendUpstreamPacket(containerOpenPacket);
    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
        //
    }

    @Override
    public void updateInventory(GeyserSession session, Inventory inventory) {
        updater.updateInventory(this, session, inventory);
    }

    @Override
    public void updateSlot(GeyserSession session, Inventory inventory, int slot) {
        updater.updateSlot(this, session, inventory, slot);
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        switch (action.getSource().getContainerId()) {
            case ContainerId.CRAFTING_ADD_INGREDIENT:
            case ContainerId.DROP_CONTENTS:
                return action.getSlot() + 1;
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
        return action.getSource().getContainerId() == ContainerId.CRAFTING_RESULT;
    }

    @Override
    protected void processAction(Transaction transaction, ActionData cursor, ActionData from, ActionData to) {
        super.processAction(transaction, cursor, from, to);
    }

    @Override
    public void translateActions(GeyserSession session, Inventory inventory, List<InventoryActionData> actions) {
        // Remove Useless Packet
        if (actions.stream().anyMatch(a -> a.getSource().getContainerId() == ContainerId.CRAFTING_USE_INGREDIENT)) {
            return;
        }

        super.translateActions(session, inventory, actions);
    }
}
