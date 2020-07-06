/*
 * EduSupport - Minecraft Educational Support for Geyser
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

package au.com.grieve.edusupport;

import au.com.grieve.edusupport.utils.MCEELoginEncryptionUtils;
import au.com.grieve.edusupport.utils.TokenManager;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.v363.Bedrock_v363;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.Event;
import org.geysermc.connector.event.events.BedrockCodecRegistryEvent;
import org.geysermc.connector.event.events.BedrockPongEvent;
import org.geysermc.connector.event.events.PluginDisableEvent;
import org.geysermc.connector.event.events.PluginEnableEvent;
import org.geysermc.connector.event.events.UpstreamPacketReceiveEvent;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;
import org.geysermc.connector.utils.LanguageUtils;

@Plugin(
        name = "EduSupport",
        version = "1.1.0-dev",
        authors = {"Bundabrg"},
        description = "Provides protocol support for Minecraft Educational Edition"
)
@Getter
public class EduSupportPlugin extends GeyserPlugin {
    @Getter
    public static EduSupportPlugin instance;

    private final TokenManager tokenManager;

    public EduSupportPlugin(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);

        instance = this;
        tokenManager = new TokenManager();
    }

    @Event
    public void onCodecRegistry(BedrockCodecRegistryEvent event) {
        event.setCodec(Bedrock_v363.V363_CODEC);
    }

    @Event
    public void onBedrockPong(BedrockPongEvent event) {
        event.getPong().setEdition("MCEE");
    }

    @Event(filter = LoginPacket.class)
    public void onLoginPacket(UpstreamPacketReceiveEvent<LoginPacket> event) {
        event.setCancelled(true);

        if (event.getPacket().getProtocolVersion() > GeyserConnector.BEDROCK_PACKET_CODEC.getProtocolVersion()) {
            event.getSession().disconnect(LanguageUtils.getPlayerLocaleString("geyser.network.outdated.server", event.getSession().getClientData().getLanguageCode(), GeyserConnector.BEDROCK_PACKET_CODEC.getMinecraftVersion()));
            return;
        } else if (event.getPacket().getProtocolVersion() < GeyserConnector.BEDROCK_PACKET_CODEC.getProtocolVersion()) {
            event.getSession().disconnect(LanguageUtils.getPlayerLocaleString("geyser.network.outdated.client", event.getSession().getClientData().getLanguageCode(), GeyserConnector.BEDROCK_PACKET_CODEC.getMinecraftVersion()));
            return;
        }

        MCEELoginEncryptionUtils.encryptPlayerConnection(getConnector(), event.getSession(), event.getPacket());

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        event.getSession().sendUpstreamPacket(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        event.getSession().sendUpstreamPacket(resourcePacksInfo);
    }

    @Event
    public void onEnable(PluginEnableEvent event) {
        System.err.println("I'm alive");
    }

    @Event
    public void onDisable(PluginDisableEvent event) {
        System.err.println("I'm dead");
    }
}
