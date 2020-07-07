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

package au.com.grieve.edusupport.network.translators.inventory.action;

import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.geysermc.connector.GeyserConnector;

import java.util.concurrent.TimeUnit;

public abstract class ConfirmAction extends BaseAction {

    protected int id;

    @Override
    public void execute() {
        id = transaction.getInventory().getTransactionId().getAndIncrement();
    }

    /**
     * Called when we received a server confirmation packet.
     */
    public void confirm(int id, boolean accepted) {
        if (id != this.id) {
            GeyserConnector.getInstance().getLogger().warning("Out of sequence Confirmation Packet with id: " + id);
            return;
        }

        if (!accepted) {
            // Downstream disagrees with what we think the slot is so we will update and accept it
            GeyserConnector.getInstance().getGeneralThreadPool().schedule(() -> {
                ClientConfirmTransactionPacket confirmPacket = new ClientConfirmTransactionPacket(transaction.getInventory().getId(),
                        id, true);
                transaction.getSession().sendDownstreamPacket(confirmPacket);

                transaction.next();
            }, 200, TimeUnit.MILLISECONDS);
            return;
        }

        transaction.next();
    }

}
