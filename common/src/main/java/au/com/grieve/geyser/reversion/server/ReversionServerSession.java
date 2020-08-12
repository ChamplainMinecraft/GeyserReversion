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

import au.com.grieve.geyser.reversion.api.BaseTranslator;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.channel.EventLoop;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Collection;

@Getter
public class ReversionServerSession extends BedrockServerSession {

    @Setter
    private GeyserSession geyserSession;

    private BaseTranslator translator;

    public ReversionServerSession(RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(connection, eventLoop, serializer);

        ReversionBatchHandler batchHandler = new ReversionBatchHandler();
        setBatchHandler(batchHandler);
    }

    public void sendPacketDirect(BedrockPacket packet) {
        super.sendPacket(packet);
    }

    @Override
    public void sendWrapped(Collection<BedrockPacket> packets, boolean encrypt) {
        for (BedrockPacket packet : packets) {
            translator.getLast().sendUpstream(packet);
        }
    }
}
