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

import au.com.grieve.geyser.reversion.editions.mcee.BaseTranslator;
import au.com.grieve.geyser.reversion.editions.mcee.EducationEdition;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.channel.EventLoop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.GeyserConnector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReversionServerSession extends BedrockServerSession {

    private List<BaseTranslator> translators = new ArrayList<>();

    public ReversionServerSession(RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(connection, eventLoop, serializer);

        ReversionBatchHandler batchHandler = new ReversionBatchHandler();
        batchHandler.getPacketHandlers().add(new VersionDetectPacketHandler());

        setBatchHandler(batchHandler);
    }

    @Getter
    @RequiredArgsConstructor
    public class VersionDetectPacketHandler implements BedrockPacketHandler {

        @Override
        public boolean handle(LoginPacket packet) {
            Class<? extends BaseTranslator> translatorClass = EducationEdition.getInstance().getTranslators().get(packet.getProtocolVersion());
            if (translatorClass != null) {
                try {
                    translator = translatorClass.getConstructor(ReversionServerSession.class).newInstance(ReversionServerSession.this);
                    EducationEdition.getInstance().getPlugin().getLogger().debug("Player connected with version: " + translator.getCodec().getMinecraftVersion());
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    EducationEdition.getInstance().getPlugin().getLogger().error("Failed to load translator", e);
                }
            }

            // Unknown Version. We will set the current codec and pass on so it can handle errors
            System.err.println("Unknown so setting codec");
            setPacketCodec(GeyserConnector.BEDROCK_PACKET_CODEC);
            return false;
        }
    }
}
