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

package au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408;

import au.com.grieve.geyser.reversion.ReversionManager;
import au.com.grieve.geyser.reversion.api.BaseTranslator;
import au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408.handlers.FromClientPacketHandler;
import au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408.handlers.FromDownstreamPacketHandler;
import au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408.handlers.FromUpstreamPacketHandler;
import au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408.handlers.ToClientPacketHandler;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.v390.Bedrock_v390;
import lombok.Getter;
import org.geysermc.connector.network.session.GeyserSession;

@Getter
public class Translator_ee390_be408 extends BaseTranslator {
    private final BedrockPacketCodec codec = Bedrock_v390.V390_CODEC;

    private final BedrockPacketHandler fromUpstreamPacketHandler;
    private final BedrockPacketHandler fromDownstreamPacketHandler;

    private final BedrockPacketHandler fromClientPacketHandler;
    private final BedrockPacketHandler toClientPacketHandler;

    public Translator_ee390_be408(ReversionManager manager, GeyserSession session) {
        super(manager, session);

        fromUpstreamPacketHandler = new FromUpstreamPacketHandler(this);
        fromDownstreamPacketHandler = new FromDownstreamPacketHandler(this);

        fromClientPacketHandler = new FromClientPacketHandler(this);
        toClientPacketHandler = new ToClientPacketHandler(this);
    }

    @Override
    public boolean fromUpstream(BedrockPacket packet) {
        if (packet.handle(fromUpstreamPacketHandler)) {
            return true;
        }
        return super.fromUpstream(packet);
    }

    @Override
    public boolean fromDownstream(BedrockPacket packet) {
        if (packet.handle(fromDownstreamPacketHandler)) {
            return true;
        }
        return super.fromDownstream(packet);
    }

    @Override
    public boolean fromClient(BedrockPacket packet) {
        if (packet.handle(fromClientPacketHandler)) {
            return true;
        }
        return super.fromClient(packet);
    }

    @Override
    public boolean toClient(BedrockPacket packet) {
        if (packet.handle(toClientPacketHandler)) {
            return true;
        }
        return super.toClient(packet);
    }
}
