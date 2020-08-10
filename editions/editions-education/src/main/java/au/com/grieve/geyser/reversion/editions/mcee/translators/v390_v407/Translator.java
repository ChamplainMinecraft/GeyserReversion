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

package au.com.grieve.geyser.reversion.editions.mcee.translators.v390_v407;

import au.com.grieve.geyser.reversion.editions.mcee.BaseTranslator;
import au.com.grieve.geyser.reversion.editions.mcee.hook.ReversionServerSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.v390.Bedrock_v390;
import lombok.Getter;

@Getter
public class Translator extends BaseTranslator {
    private final BedrockPacketCodec codec = Bedrock_v390.V390_CODEC;

    private final BedrockPacketHandler upstreamPacketHandler;
    private final BedrockPacketHandler downstreamPacketHandler;

    public Translator(ReversionServerSession session) {
        super(session);

        upstreamPacketHandler = new UpstreamPacketHandler(this);
        downstreamPacketHandler = new DownstreamPacketHandler(this);
    }

    @Override
    public boolean handleUpstream(BedrockPacket packet) {
        return packet.handle(upstreamPacketHandler);
    }

    @Override
    public boolean handleDownstream(BedrockPacket packet) {
        return packet.handle(downstreamPacketHandler);
    }
}
