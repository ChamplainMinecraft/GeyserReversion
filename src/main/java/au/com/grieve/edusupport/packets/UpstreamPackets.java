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

package au.com.grieve.edusupport.packets;

import au.com.grieve.edusupport.EduSupportPlugin;
import au.com.grieve.edusupport.utils.MCEELoginEncryptionUtils;
import com.nukkitx.protocol.bedrock.data.GameRuleData;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.CreativeContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.Event;
import org.geysermc.connector.event.events.UpstreamPacketReceiveEvent;
import org.geysermc.connector.event.events.UpstreamPacketSendEvent;
import org.geysermc.connector.utils.LanguageUtils;

public class UpstreamPackets {

    /**
     * Override Login so we can use our own Encryption
     *
     * @param event
     */
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

        MCEELoginEncryptionUtils.encryptPlayerConnection(EduSupportPlugin.getInstance().getConnector(), event.getSession(), event.getPacket());

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        event.getSession().sendUpstreamPacket(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        event.getSession().sendUpstreamPacket(resourcePacksInfo);
    }

    /**
     * MCEE doesn't have a CreativeContent Packet
     * <p>
     * We replace it with an Inventory Packet with a container type of Creative
     */
    @Event(filter = CreativeContentPacket.class)
    public void onCreativeContentPacketSend(UpstreamPacketSendEvent<CreativeContentPacket> event) {
        event.setCancelled(true);

        InventoryContentPacket packet = new InventoryContentPacket();
        packet.setContainerId(ContainerId.CREATIVE);
        packet.setContents(event.getPacket().getEntries().values().toArray(new ItemData[0]));
        event.getSession().sendUpstreamPacketImmediately(packet);
    }

    /**
     * Modify StartGamePacket being sent
     */
    @Event(filter = StartGamePacket.class)
    public void onStartGamePacketSend(UpstreamPacketSendEvent<StartGamePacket> event) {
        // Disable CodeBuilder
        event.getPacket().getGamerules().add(new GameRuleData<>("codebuilder", false));

//        // Add MCEE Palette
//        try(NBTInputStream stream = NbtUtils.createNetworkReader(EduSupportPlugin.getInstance().getResourceAsStream("bedrock/runtime_block_states.dat"))) {
//            //noinspection unchecked
//            event.getPacket().setBlockPalette((NbtList<NbtMap>) stream.readTag());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
