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

import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;

/**
 * Wrapper for BedrockServerEventHandler
 */
@ParametersAreNonnullByDefault
public class ReversionServerEventHandler implements BedrockServerEventHandler {
    private final BedrockServerEventHandler original;

    public ReversionServerEventHandler(BedrockServerEventHandler original) {
        this.original = original;
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
        return original.onConnectionRequest(inetSocketAddress);
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress inetSocketAddress) {
        return original.onQuery(inetSocketAddress);
    }

    @Override
    public void onSessionCreation(BedrockServerSession bedrockServerSession) {
        original.onSessionCreation(bedrockServerSession);
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext ctx, DatagramPacket packet) {
        original.onUnhandledDatagram(ctx, packet);

    }
}
