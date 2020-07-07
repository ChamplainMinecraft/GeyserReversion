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

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.data.inventory.InventoryActionData;
import com.nukkitx.protocol.bedrock.packet.ContainerSetDataPacket;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.updater.ContainerInventoryUpdater;

public class FurnaceInventoryTranslator extends BlockInventoryTranslator {

    public FurnaceInventoryTranslator() {
        super(3, "minecraft:furnace[facing=north,lit=false]", ContainerType.FURNACE, new ContainerInventoryUpdater());
    }

    @Override
    public void updateProperty(GeyserSession session, Inventory inventory, int key, int value) {
        ContainerSetDataPacket dataPacket = new ContainerSetDataPacket();
        dataPacket.setWindowId((byte) inventory.getId());
        switch (key) {
            case 0:
                dataPacket.setProperty(ContainerSetDataPacket.FURNACE_LIT_TIME);
                break;
            case 1:
                dataPacket.setProperty(ContainerSetDataPacket.FURNACE_LIT_DURATION);
                break;
            case 2:
                dataPacket.setProperty(ContainerSetDataPacket.FURNACE_TICK_COUNT);
                if (inventory.getWindowType() == WindowType.BLAST_FURNACE || inventory.getWindowType() == WindowType.SMOKER) {
                    value *= 2;
                }
                break;
            default:
                return;
        }
        dataPacket.setValue(value);
        session.sendUpstreamPacket(dataPacket);
    }

    @Override
    public boolean isOutput(InventoryActionData action) {
        return bedrockSlotToJava(action) == 2;
    }
}
