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

package au.com.grieve.geyser.reversion.api;

import au.com.grieve.geyser.reversion.server.ReversionServerSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

/**
 * A translator translates from one version to another. It may chain to another translator
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseTranslator {

    @Setter
    public BaseTranslator next;

    @Setter
    public BaseTranslator previous;

    private final GeyserSession session;

    public abstract BedrockPacketCodec getCodec();

    public BaseTranslator getLast() {
        return next == null ? this : next.getLast();
    }

    public BaseTranslator getFirst() {
        return previous == null ? this : previous.getFirst();
    }

    /**
     * The name of this translator
     *
     * @return the string name
     */
    public boolean receiveUpstream(BedrockPacket packet) {
        return sendDownstream(packet);
    }

    public boolean receiveDownstream(BedrockPacket packet) {
        return sendUpstream(packet);
    }

    public boolean sendUpstream(BedrockPacket packet) {
        // Try pass to next in chain
        if (previous != null) {
            return previous.receiveDownstream(packet);
        }

        // Send out
        System.err.println("out: " + packet);
        ((ReversionServerSession) session.getUpstream().getSession()).sendPacketDirect(packet);
        return true;
    }

    public boolean sendDownstream(BedrockPacket packet) {
        // Try pass to previous in chain
        if (next != null) {
            return next.receiveUpstream(packet);
        }

        // Pass to Geyser
        System.err.println(packet);
        return session.receiveUpstreamPacket(packet);
    }

    @Getter
    public static class DefaultTranslator extends BaseTranslator {
        BedrockPacketCodec codec = GeyserConnector.BEDROCK_PACKET_CODEC;

        public DefaultTranslator(GeyserSession session) {
            super(session);
        }
    }

}
