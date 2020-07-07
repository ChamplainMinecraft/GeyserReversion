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

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.holder.BlockInventoryHolder;
import org.geysermc.connector.network.translators.inventory.holder.InventoryHolder;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

public class SingleChestInventoryTranslator extends ChestInventoryTranslator {
    private InventoryHolder holder;

    public SingleChestInventoryTranslator(int size) {
        super(size, 27);
    }

    private InventoryHolder getHolder() {
        if (holder == null) {
            int javaBlockState = BlockTranslator.getJavaBlockState("minecraft:chest[facing=north,type=single,waterlogged=false]");
            holder = new BlockInventoryHolder(BlockTranslator.getBedrockBlockId(javaBlockState), ContainerType.CONTAINER);
        }
        return holder;
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
}
