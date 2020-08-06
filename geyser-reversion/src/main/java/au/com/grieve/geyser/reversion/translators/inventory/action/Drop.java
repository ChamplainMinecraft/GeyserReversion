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

package au.com.grieve.geyser.reversion.translators.inventory.action;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.window.DropItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

/**
 * Send a Drop packet to the Downstream server
 */
@Getter
@ToString
@AllArgsConstructor
public class Drop extends ConfirmAction {

    private final Type dropType;
    private final int javaSlot;

    @Override
    public void execute() {
        super.execute();

        int slot;

        switch (dropType) {
            case DROP_ITEM:
            case DROP_STACK:
                ClientWindowActionPacket dropPacket = new ClientWindowActionPacket(
                        transaction.getInventory().getId(),
                        id,
                        javaSlot,
                        null,
                        WindowAction.DROP_ITEM,
                        dropType == Type.DROP_ITEM ? DropItemParam.DROP_FROM_SELECTED : DropItemParam.DROP_SELECTED_STACK
                );
                transaction.getSession().sendDownstreamPacket(dropPacket);
                slot = javaSlot;
                break;
            case DROP_ITEM_HOTBAR:
            case DROP_STACK_HOTBAR:
                ClientPlayerActionPacket actionPacket = new ClientPlayerActionPacket(
                        dropType == Type.DROP_ITEM_HOTBAR ? PlayerAction.DROP_ITEM : PlayerAction.DROP_ITEM_STACK,
                        new Position(0, 0, 0),
                        BlockFace.DOWN
                );
                transaction.getSession().sendDownstreamPacket(actionPacket);
                slot = transaction.getSession().getInventory().getHeldItemSlot();
                break;
            default:
                transaction.next();
                return;
        }

        // Update Inventory
        ItemStack item = transaction.getSession().getInventory().getItem(slot);
        if (item != null) {
            switch (dropType) {
                case DROP_ITEM:
                case DROP_ITEM_HOTBAR:
                    transaction.getSession().getInventory().setItem(slot, new ItemStack(item.getId(), item.getAmount() - 1, item.getNbt()));
                    break;
                case DROP_STACK:
                case DROP_STACK_HOTBAR:
                    transaction.getSession().getInventory().setItem(slot, new ItemStack(BlockTranslator.AIR));
                    break;
            }
        }

        transaction.next();
    }

    public enum Type {
        DROP_ITEM,
        DROP_STACK,
        DROP_ITEM_HOTBAR,
        DROP_STACK_HOTBAR
    }
}
