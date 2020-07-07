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

import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.updater.ChestInventoryUpdater;
import org.geysermc.connector.network.translators.inventory.updater.InventoryUpdater;
import org.geysermc.connector.utils.InventoryUtils;

import java.util.List;

public abstract class ChestInventoryTranslator extends BaseInventoryTranslator {
    private final InventoryUpdater updater;

    public ChestInventoryTranslator(int size, int paddedSize) {
        super(size);
        this.updater = new ChestInventoryUpdater(paddedSize);
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
        for (InventoryActionData action : actions) {
            if (action.getSource().getContainerId() == inventory.getId()) {
                if (action.getSlot() >= size) {
                    updateInventory(session, inventory);
                    InventoryUtils.updateCursor(session);
                    return;
                }
            }
        }

        super.translateActions(session, inventory, actions);
    }
}
