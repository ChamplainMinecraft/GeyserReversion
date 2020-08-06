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

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import org.geysermc.connector.network.translators.inventory.updater.CursorInventoryUpdater;

public class SmithingInventoryTranslator extends BlockInventoryTranslator {

    public SmithingInventoryTranslator() {
        super(3, "minecraft:smithing_table", ContainerType.SMITHING_TABLE, new CursorInventoryUpdater());
    }

    @Override
    public int bedrockSlotToJava(InventoryActionData action) {
        final int slot = super.bedrockSlotToJava(action);
        if (action.getSource().getContainerId() == ContainerId.UI) {
            switch (slot) {
                case 51:
                    return 0;
                case 52:
                    return 1;
                case 50:
                    return 2;
                default:
                    return slot;
            }
        }
        return slot;
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        switch (slot) {
            case 0:
                return 51;
            case 1:
                return 52;
            case 2:
                return 50;
        }
        return super.javaSlotToBedrock(slot);
    }

}
