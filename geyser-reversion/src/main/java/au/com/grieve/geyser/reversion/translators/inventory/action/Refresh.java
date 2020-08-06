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

import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import lombok.Getter;
import lombok.ToString;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.utils.InventoryUtils;

import java.util.concurrent.TimeUnit;

/**
 * Send an invalid click to refresh all slots
 * <p>
 * We will filter out repeat refreshes and ensre our executation happens last in the plan
 */
@Getter
@ToString
public class Refresh extends ConfirmAction {

    private final int weight = 10;

    @Override
    public void execute() {
        super.execute();

        ClientWindowActionPacket clickPacket = new ClientWindowActionPacket(transaction.getInventory().getId(),
                id, -1, InventoryUtils.REFRESH_ITEM,
                WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);

        transaction.getSession().sendDownstreamPacket(clickPacket);
    }


    @Override
    public void confirm(int id, boolean accepted) {
        if (id != this.id) {
            GeyserConnector.getInstance().getLogger().warning("Out of sequence Confirmation Packet with id: " + id);
            return;
        }

        // We always reject the packet, but we will wait a little bit for a resync
        GeyserConnector.getInstance().getGeneralThreadPool().schedule(() -> {
            InventoryUtils.updateCursor(transaction.getSession());
            transaction.getTranslator().updateInventory(transaction.getSession(), transaction.getInventory());


            ClientConfirmTransactionPacket confirmPacket = new ClientConfirmTransactionPacket(transaction.getInventory().getId(),
                    id, false);
            transaction.getSession().sendDownstreamPacket(confirmPacket);

            transaction.next();
        }, 200, TimeUnit.MILLISECONDS);
    }
}
