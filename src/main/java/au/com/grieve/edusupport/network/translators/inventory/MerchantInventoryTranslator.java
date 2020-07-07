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

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.updater.ContainerInventoryUpdater;
import org.geysermc.connector.network.translators.inventory.updater.InventoryUpdater;

import java.util.List;

public class MerchantInventoryTranslator extends BaseInventoryTranslator {

    private final InventoryUpdater updater;

    public MerchantInventoryTranslator() {
        super(3);
        this.updater = new ContainerInventoryUpdater();
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        if (slot < size) {
            return slot;
        }
        return super.javaSlotToBedrock(slot);
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        switch (action.getSource().getContainerId()) {
            case -28: // Trading 1?
                return 0;
            case -29: // Trading 2?
                return 1;
            case -30: // Trading Output?
                return 2;
        }
        return super.bedrockSlotToJava(action);
    }

    @Override
    public boolean isOutput(InventoryActionData action) {
        return action.getSource().getContainerId() == ContainerId.TRADING_OUTPUT && action.getSlot() == 0;
    }

    @Override
    public void prepareInventory(GeyserSession session, Inventory inventory) {

    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {

    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
        session.setLastInteractedVillagerEid(-1);
        session.setVillagerTrades(null);
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
    public void translateActions(GeyserSession session, Inventory inventory, List<InventoryActionData> actions) {
        if (actions.stream().anyMatch(a -> a.getSource().getContainerId() == -31)) {
            return;
        }

        super.translateActions(session, inventory, actions);
    }
}
