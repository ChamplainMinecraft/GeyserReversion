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
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.handler.BatchHandler;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This provides a multi-layer batch handler
 * <p>
 * 1. Each of the packetHandlers are executed in turn. If any return true then we stop
 * 2. If a translator is set then pass to its handleFromUpstream. If it returns true we stop.
 * 3. Finally the session packet handler is executed, logging if it returns false
 */
@Getter
@RequiredArgsConstructor
public class ReversionBatchHandler implements BatchHandler {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(ReversionBatchHandler.class);

    private final ConcurrentLinkedDeque<BedrockPacketHandler> packetHandlers = new ConcurrentLinkedDeque<>();

    @Override
    public void handle(BedrockSession session, ByteBuf compressed, Collection<BedrockPacket> packets) {
        for (BedrockPacket packet : packets) {
            if (session.isLogging() && log.isTraceEnabled()) {
                log.trace("Inbound {}: {}", session.getAddress(), packet);
            }

            for (BedrockPacketHandler handler : packetHandlers) {
                if (packet.handle(handler)) {
                    return;
                }
            }

            if (session instanceof ReversionServerSession) {
                BaseTranslator translator = ((ReversionServerSession) session).getTranslator();
                if (translator != null && translator.fromUpstream(packet)) {
                    return;
                }
            } else {
                BedrockPacketHandler handler = session.getPacketHandler();
                if (handler == null || !packet.handle(handler)) {
                    log.debug("Unhandled packet for {}: {}", session.getAddress(), packet);
                }
            }
        }
    }
}
