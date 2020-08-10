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

package au.com.grieve.geyser.reversion.versions.mcee.hook;

import com.nukkitx.network.raknet.RakNetServerListener;
import com.nukkitx.network.raknet.RakNetServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;

/**
 * Wrapper for Geyser Raknet Listener
 */
@ParametersAreNonnullByDefault
public class ReversionRaknetListener implements RakNetServerListener {
    private final RakNetServerListener original;

    public ReversionRaknetListener(RakNetServerListener original) {
        this.original = original;
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
        return original.onConnectionRequest(inetSocketAddress);
    }

    @Override
    public byte[] onQuery(InetSocketAddress inetSocketAddress) {
        return original.onQuery(inetSocketAddress);
    }

    @Override
    public void onSessionCreation(RakNetServerSession rakNetServerSession) {
        // Create our own session here
        original.onSessionCreation(rakNetServerSession);
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {
        original.onUnhandledDatagram(channelHandlerContext, datagramPacket);

    }
}
