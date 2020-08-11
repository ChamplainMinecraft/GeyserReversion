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

package au.com.grieve.geyser.reversion.editions.mcee.hook;

import com.nukkitx.network.raknet.RakNetServerListener;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.EventLoops;
import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockRakNetSessionListener;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.compat.BedrockCompat;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.UpstreamPacketHandler;
import org.geysermc.connector.network.session.GeyserSession;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

@Getter
public class ReversionBedrockServer extends BedrockServer {
    protected final EventLoopGroup eventLoopGroup;
    private final BedrockServer original;

    public ReversionBedrockServer(BedrockServer original) {
        super(original.getBindAddress(), 1, EventLoops.commonGroup());

        this.original = original;
        eventLoopGroup = EventLoops.commonGroup();
        getRakNet().setListener(new ReversionServerListener(getRakNet().getListener()));
        setHandler(new ReversionServerEventHandler(original.getHandler()));
    }

    @ParametersAreNonnullByDefault
    protected static class ReversionServerEventHandler implements BedrockServerEventHandler {
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
            BedrockPong pong = original.onQuery(inetSocketAddress);
            if (pong != null) {
                pong.setEdition("MCEE");
                pong.setProtocolVersion(BedrockCompat.COMPAT_CODEC.getProtocolVersion());
            }

            return pong;
        }

        @Override
        public void onSessionCreation(BedrockServerSession bedrockSession) {
            // Handled Elsewhere
        }

        @Override
        public void onUnhandledDatagram(ChannelHandlerContext ctx, DatagramPacket packet) {
            original.onUnhandledDatagram(ctx, packet);

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
        public byte[] onQuery(InetSocketAddress inetSocketAddress) {
            return original.onQuery(inetSocketAddress);
        }

        @Override
        public void onSessionCreation(RakNetServerSession connection) {
            BedrockWrapperSerializer serializer = BedrockWrapperSerializers.getSerializer(connection.getProtocolVersion());
            ReversionServerSession bedrockSession = new ReversionServerSession(connection, ReversionBedrockServer.this.eventLoopGroup.next(), serializer);

            BedrockRakNetSessionListener.Server server;
            try {
                Constructor<BedrockRakNetSessionListener.Server> constructor = BedrockRakNetSessionListener.Server.class
                        .getDeclaredConstructor(BedrockServerSession.class, RakNetSession.class, BedrockServer.class);

                constructor.setAccessible(true);
                server = constructor.newInstance(bedrockSession, connection, ReversionBedrockServer.this);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                connection.disconnect(DisconnectReason.CLOSED_BY_REMOTE_PEER);
                return;
            }

            connection.setListener(server);

            GeyserSession session = new GeyserSession(GeyserConnector.getInstance(), bedrockSession);
            bedrockSession.setGeyserSession(session);

            bedrockSession.setLogging(true);
            bedrockSession.setPacketHandler(new UpstreamPacketHandler(GeyserConnector.getInstance(), session));
            bedrockSession.setPacketCodec(BedrockCompat.COMPAT_CODEC);
        }

        @Override
        public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {
            original.onUnhandledDatagram(channelHandlerContext, datagramPacket);

        }
    }

}
