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

import au.com.grieve.geyser.reversion.ReversionManager;
import au.com.grieve.geyser.reversion.server.ReversionServerSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.session.GeyserSession;

/**
 * A translator translates from one version to another. It may chain to another translator.
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseTranslator {

    /**
     * Next Translator in chain. If null then we are the closest to Geyser
     *
     * @param next Translater to set as next
     * @return the next translator
     */
    @Setter
    public BaseTranslator next;

    /**
     * Previous Translator in chain. If null then we are the closest to the Client
     *
     * @param previous Translator to set as previous
     * @return the previous translator
     */
    @Setter
    public BaseTranslator previous;

    private final ReversionManager manager;
    private final GeyserSession session;

    public abstract BedrockPacketCodec getCodec();

    /**
     * Get the last translator in the chain
     *
     * @return the last translator
     */
    public BaseTranslator getLast() {
        return next == null ? this : next.getLast();
    }

    /**
     * Get the first translator in the chain
     *
     * @return the first translator
     */
    public BaseTranslator getFirst() {
        return previous == null ? this : previous.getFirst();
    }

    /**
     * Receive a packet from upstream
     *
     * @param packet Packet to receive
     * @return true if handled
     */
    public boolean fromUpstream(BedrockPacket packet) {
        return toDownstream(packet);
    }

    /**
     * Receive a packet from downstream
     *
     * @param packet Packet to receive
     * @return true if handled
     */
    public boolean fromDownstream(BedrockPacket packet) {
        return toUpstream(packet);
    }

    /**
     * Receive a packet from Geyser
     * <p>
     * This is only executed if this translator is the end in the chain
     *
     * @param packet Packet to receieve
     * @return true if handled
     */
    public boolean fromGeyser(BedrockPacket packet) {
        return fromDownstream(packet);
    }


    /**
     * Receive a packet from Client
     * <p>
     * This is only executed if this translator is the first in the chain
     *
     * @param packet Packet to receive
     * @return true if handled
     */
    public boolean fromClient(BedrockPacket packet) {
        return fromUpstream(packet);
    }

    /**
     * Send a packet to our upstream
     *
     * @param packet Packet to send
     * @return true if handled
     */
    public boolean toUpstream(BedrockPacket packet) {
        // Try pass to previous in chain
        if (previous != null) {
            return previous.fromDownstream(packet);
        }

        // Send to Client
        return toClient(packet);
    }

    /**
     * Send a packet to our downstream
     *
     * @param packet Packet to send
     * @return true if handled
     */
    public boolean toDownstream(BedrockPacket packet) {
        // Try pass to next in chain
        if (next != null) {
            return next.fromUpstream(packet);
        }

        // Pass to Geyser
        return toGeyser(packet);
    }

    /**
     * Send a packet to Geyser
     * <p>
     * This is only executed when we are the last in the chain
     *
     * @param packet Packet to send
     * @return true if handled
     */
    public boolean toGeyser(BedrockPacket packet) {
        return session.receiveUpstreamPacket(packet);
    }

    /**
     * Send a packet to the Client
     * <p>
     * This is only executed when we are the first in the chain
     *
     * @param packet Packet to send
     * @return true if handled
     */
    public boolean toClient(BedrockPacket packet) {
        ((ReversionServerSession) session.getUpstream().getSession()).sendPacketDirect(packet);

        // Always handled
        return true;
    }

    @Getter
    public static class DefaultTranslator extends BaseTranslator {
        BedrockPacketCodec codec = BedrockProtocol.DEFAULT_BEDROCK_CODEC;

        public DefaultTranslator(ReversionManager manager, GeyserSession session) {
            super(manager, session);
        }
    }

}
