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

package au.com.grieve.geyser.reversion.server;

import com.nukkitx.network.raknet.RakNetServerListener;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.EventLoops;
import com.nukkitx.protocol.bedrock.BedrockRakNetSessionListener;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

/**
 * A BedrockServer implementation that wraps the existing BedrockServer as far as possible
 */
@Getter
public class ReversionServer extends BedrockServer {
    private final BedrockServer original;

    public ReversionServer(BedrockServer original) {
        super(original.getBindAddress());

        this.original = original;
        getRakNet().setListener(new ReversionServerListener(original.getRakNet().getListener()));
        setHandler(new ReversionServerEventHandler(original.getHandler()));
    }

    protected EventLoopGroup getEventLoopGroup() {
        try {
            return (EventLoopGroup) getClass().getDeclaredMethod("getEventLoopGroup").invoke(this);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return EventLoops.commonGroup();
        }
    }

    @ParametersAreNonnullByDefault
    protected class ReversionServerListener implements RakNetServerListener {
        private final RakNetServerListener original;

        public ReversionServerListener(RakNetServerListener original) {
            this.original = original;
        }

        @Override
        public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
            return original.onConnectionRequest(inetSocketAddress);
        }

        @Override
        public byte[] onQuery(InetSocketAddress address) {
            return original.onQuery(address);
        }

        @Override
        public void onSessionCreation(RakNetServerSession connection) {
            BedrockWrapperSerializer serializer = BedrockWrapperSerializers.getSerializer(connection.getProtocolVersion());
            ReversionServerSession bedrockSession = new ReversionServerSession(connection, getEventLoopGroup().next(), serializer);

            BedrockRakNetSessionListener.Server server;
            try {
                Constructor<BedrockRakNetSessionListener.Server> constructor = BedrockRakNetSessionListener.Server.class
                        .getDeclaredConstructor(BedrockServerSession.class, RakNetSession.class, BedrockServer.class);

                constructor.setAccessible(true);
                server = constructor.newInstance(bedrockSession, connection, ReversionServer.this);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                connection.disconnect(DisconnectReason.CLOSED_BY_REMOTE_PEER);
                return;
            }

            connection.setListener(server);
        }

        @Override
        public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {
            original.onUnhandledDatagram(channelHandlerContext, datagramPacket);

        }
    }

}
