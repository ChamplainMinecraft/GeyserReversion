/*
 * EduSupport - Minecraft Protocol Support for MultiVersion in Geyser
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

package au.com.grieve.geyser.reversion.translators.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.holder.BlockInventoryHolder;
import org.geysermc.connector.network.translators.inventory.holder.InventoryHolder;
import org.geysermc.connector.network.translators.inventory.updater.InventoryUpdater;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

public class BlockInventoryTranslator extends BaseInventoryTranslator {
    private final InventoryUpdater updater;
    private final String javaBlockIdentifier;
    private final ContainerType containerType;
    private InventoryHolder holder;

    public BlockInventoryTranslator(int size, String javaBlockIdentifier, ContainerType containerType, InventoryUpdater updater) {
        super(size);
        this.javaBlockIdentifier = javaBlockIdentifier;
        this.containerType = containerType;
        this.updater = updater;
    }

    private InventoryHolder getHolder() {
        if (holder == null) {
            int javaBlockState = BlockTranslator.getJavaBlockState(javaBlockIdentifier);
            int blockId = BlockTranslator.getBedrockBlockId(javaBlockState);
            this.holder = new BlockInventoryHolder(blockId, containerType);
        }
        return this.holder;
    }

    @Override
    public void prepareInventory(GeyserSession session, Inventory inventory) {
        getHolder().prepareInventory(this, session, inventory);
    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {
        getHolder().openInventory(this, session, inventory);
    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
        getHolder().closeInventory(this, session, inventory);
    }

    @Override
    public void updateInventory(GeyserSession session, Inventory inventory) {
        updater.updateInventory(this, session, inventory);
    }

    @Override
    public void updateSlot(GeyserSession session, Inventory inventory, int slot) {
        updater.updateSlot(this, session, inventory, slot);
    }
}
