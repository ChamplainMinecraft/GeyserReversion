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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

public class DoubleChestInventoryTranslator extends ChestInventoryTranslator {
    private Integer blockId;

    public DoubleChestInventoryTranslator(int size) {
        super(size, 54);
    }

    private int getBlockId() {
        if (blockId == null) {
            int javaBlockState = BlockTranslator.getJavaBlockState("minecraft:chest[facing=north,type=single,waterlogged=false]");
            blockId = BlockTranslator.getBedrockBlockId(javaBlockState);
        }
        return blockId;
    }

    @Override
    public void prepareInventory(GeyserSession session, Inventory inventory) {
        Vector3i position = session.getPlayerEntity().getPosition().toInt().add(Vector3i.UP);
        Vector3i pairPosition = position.add(Vector3i.UNIT_X);

        UpdateBlockPacket blockPacket = new UpdateBlockPacket();
        blockPacket.setDataLayer(0);
        blockPacket.setBlockPosition(position);
        blockPacket.setRuntimeId(getBlockId());
        blockPacket.getFlags().addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY);
        session.sendUpstreamPacket(blockPacket);

        NbtMap tag = NbtMap.builder()
                .putString("id", "Chest")
                .putInt("x", position.getX())
                .putInt("y", position.getY())
                .putInt("z", position.getZ())
                .putInt("pairx", pairPosition.getX())
                .putInt("pairz", pairPosition.getZ())
                .putString("CustomName", inventory.getTitle()).build();
        BlockEntityDataPacket dataPacket = new BlockEntityDataPacket();
        dataPacket.setData(tag);
        dataPacket.setBlockPosition(position);
        session.sendUpstreamPacket(dataPacket);

        blockPacket = new UpdateBlockPacket();
        blockPacket.setDataLayer(0);
        blockPacket.setBlockPosition(pairPosition);
        blockPacket.setRuntimeId(getBlockId());
        blockPacket.getFlags().addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY);
        session.sendUpstreamPacket(blockPacket);

        tag = NbtMap.builder()
                .putString("id", "Chest")
                .putInt("x", pairPosition.getX())
                .putInt("y", pairPosition.getY())
                .putInt("z", pairPosition.getZ())
                .putInt("pairx", position.getX())
                .putInt("pairz", position.getZ())
                .putString("CustomName", inventory.getTitle()).build();
        dataPacket = new BlockEntityDataPacket();
        dataPacket.setData(tag);
        dataPacket.setBlockPosition(pairPosition);
        session.sendUpstreamPacket(dataPacket);

        inventory.setHolderPosition(position);
    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) inventory.getId());
        containerOpenPacket.setType(ContainerType.CONTAINER);
        containerOpenPacket.setBlockPosition(inventory.getHolderPosition());
        containerOpenPacket.setUniqueEntityId(inventory.getHolderId());
        session.sendUpstreamPacket(containerOpenPacket);
    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
        Vector3i holderPos = inventory.getHolderPosition();
        Position pos = new Position(holderPos.getX(), holderPos.getY(), holderPos.getZ());
        int realBlock = session.getConnector().getWorldManager().getBlockAt(session, pos.getX(), pos.getY(), pos.getZ());
        UpdateBlockPacket blockPacket = new UpdateBlockPacket();
        blockPacket.setDataLayer(0);
        blockPacket.setBlockPosition(holderPos);
        blockPacket.setRuntimeId(BlockTranslator.getBedrockBlockId(realBlock));
        session.sendUpstreamPacket(blockPacket);

        holderPos = holderPos.add(Vector3i.UNIT_X);
        pos = new Position(holderPos.getX(), holderPos.getY(), holderPos.getZ());
        realBlock = session.getConnector().getWorldManager().getBlockAt(session, pos.getX(), pos.getY(), pos.getZ());
        blockPacket = new UpdateBlockPacket();
        blockPacket.setDataLayer(0);
        blockPacket.setBlockPosition(holderPos);
        blockPacket.setRuntimeId(BlockTranslator.getBedrockBlockId(realBlock));
        session.sendUpstreamPacket(blockPacket);
    }
}
