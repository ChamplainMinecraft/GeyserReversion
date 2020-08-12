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

import au.com.grieve.geyser.reversion.editions.mcee.utils.MCEELoginEncryptionUtils;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.GeyserConnector;

@Getter
@RequiredArgsConstructor
public class UpstreamPacketHandler implements BedrockPacketHandler {
    private final Translator_ee390_be408 translator;

    @Override
    public boolean handle(LoginPacket packet) {
        System.err.println(packet);
        MCEELoginEncryptionUtils.encryptPlayerConnection(GeyserConnector.getInstance(), translator.getSession(), packet);

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        translator.sendUpstream(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        translator.sendUpstream(resourcePacksInfo);
        return true;
    }
}
