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

import au.com.grieve.geyser.reversion.api.BaseTranslator;
import au.com.grieve.geyser.reversion.api.UpstreamSession;
import au.com.grieve.geyser.reversion.editions.mcee.EducationEdition;
import au.com.grieve.geyser.reversion.editions.mcee.exceptions.TranslatorException;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.channel.EventLoop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Collection;

@Getter
public class ReversionServerSession extends BedrockServerSession implements UpstreamSession {

    @Setter
    private GeyserSession geyserSession;

    private BaseTranslator translator;

    public ReversionServerSession(RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(connection, eventLoop, serializer);

        ReversionBatchHandler batchHandler = new ReversionBatchHandler();
        batchHandler.getPacketHandlers().add(new VersionDetectPacketHandler());

        setBatchHandler(batchHandler);
    }

    @Override
    public void sendPacketDirect(BedrockPacket packet) {
        super.sendPacket(packet);
    }

    @Override
    public void sendWrapped(Collection<BedrockPacket> packets, boolean encrypt) {
        for (BedrockPacket packet : packets) {
            translator.sendUpstream(packet);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class VersionDetectPacketHandler implements BedrockPacketHandler {

        @Override
        public boolean handle(LoginPacket packet) {
            try {
                translator = EducationEdition.getInstance().createTranslator(
                        packet.getProtocolVersion(), GeyserConnector.BEDROCK_PACKET_CODEC.getProtocolVersion(), geyserSession);
                EducationEdition.getInstance().getPlugin().getLogger().debug("Player connected with version: " + translator.getCodec().getMinecraftVersion());
                setPacketCodec(translator.getCodec());
                return false;
            } catch (TranslatorException e) {
                EducationEdition.getInstance().getPlugin().getLogger().error("Failed to load Version Translation", e);
            }

            // Unknown Version. We will set the current codec and pass on instead
            System.err.println("Unknown version so assuming latest");
            setPacketCodec(GeyserConnector.BEDROCK_PACKET_CODEC);
            return false;
        }
    }
}
