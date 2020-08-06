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

package au.com.grieve.geyser.reversion;

import au.com.grieve.geyser.reversion.commands.EducationCommand;
import au.com.grieve.geyser.reversion.packets.UpstreamPackets;
import au.com.grieve.geyser.reversion.registry.BlockTranslatorRegistry;
import au.com.grieve.geyser.reversion.utils.TokenManager;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v363.Bedrock_v363;
import lombok.Getter;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.network.BedrockPongEvent;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;

@Plugin(
        name = "EduSupport",
        version = "1.1.0-dev",
        authors = {"Bundabrg"},
        description = "Provides protocol support for Minecraft Educational Edition"
)
@Getter
public class EduSupportPlugin extends GeyserPlugin {
    @Getter
    private static EduSupportPlugin instance;
    private final BedrockPacketCodec upstreamCodec = Bedrock_v363.V363_CODEC;
    private final TokenManager tokenManager;

    public EduSupportPlugin(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);

        instance = this;
        tokenManager = new TokenManager();
        registerEvents(new UpstreamPackets());
    }

//    @GeyserEventHandler
//    public void onCodecRegistry(BedrockCodecRegistryEvent event) {
//        event.setCodec(Bedrock_v363.V363_CODEC);
//    }

    @GeyserEventHandler
    public void onBedrockPong(BedrockPongEvent event) {
        event.getPong().setEdition("MCEE");
        event.getPong().setProtocolVersion();
    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        getConnector().getBootstrap().getGeyserCommandManager().registerCommand(new EducationCommand(getConnector(), "education", "Education Commands", "geyser.command.education", tokenManager));

        // Register BlockMappings
        BlockTranslatorRegistry blockMapperManager = new BlockTranslatorRegistry(this).execute();

        // Register Inventory Translators


        //System.err.println(BlockTranslator.BLOCKS);System.exit(1);

    }

/*    @Event
    public void onInventoryTranslatorRegistry(InventoryTranslatorRegistryEvent event) {
        Map<WindowType, InventoryTranslator> translators = event.getRegisteredTranslators();

        translators.put(null, new PlayerInventoryTranslator()); //player inventory
        translators.put(WindowType.GENERIC_9X1, new SingleChestInventoryTranslator(9));
        translators.put(WindowType.GENERIC_9X2, new SingleChestInventoryTranslator(18));
        translators.put(WindowType.GENERIC_9X3, new SingleChestInventoryTranslator(27));
        translators.put(WindowType.GENERIC_9X4, new DoubleChestInventoryTranslator(36));
        translators.put(WindowType.GENERIC_9X5, new DoubleChestInventoryTranslator(45));
        translators.put(WindowType.GENERIC_9X6, new DoubleChestInventoryTranslator(54));
        translators.put(WindowType.BREWING_STAND, new BrewingInventoryTranslator());
        translators.put(WindowType.ANVIL, new AnvilInventoryTranslator());
        translators.put(WindowType.CRAFTING, new CraftingInventoryTranslator());
        translators.put(WindowType.GRINDSTONE, new GrindstoneInventoryTranslator());
        translators.put(WindowType.MERCHANT, new MerchantInventoryTranslator());
        translators.put(WindowType.SMITHING, new SmithingInventoryTranslator());
        //put(WindowType.ENCHANTMENT, new EnchantmentInventoryTranslator()); //TODO

        InventoryTranslator furnace = new FurnaceInventoryTranslator();
        translators.put(WindowType.FURNACE, furnace);
        translators.put(WindowType.BLAST_FURNACE, furnace);
        translators.put(WindowType.SMOKER, furnace);

        InventoryUpdater containerUpdater = new ContainerInventoryUpdater();
        translators.put(WindowType.GENERIC_3X3, new BlockInventoryTranslator(9, "minecraft:dispenser[facing=north,triggered=false]", ContainerType.DISPENSER, containerUpdater));
        translators.put(WindowType.HOPPER, new BlockInventoryTranslator(5, "minecraft:hopper[enabled=false,facing=down]", ContainerType.HOPPER, containerUpdater));
        translators.put(WindowType.SHULKER_BOX, new BlockInventoryTranslator(27, "minecraft:shulker_box[facing=north]", ContainerType.CONTAINER, containerUpdater));
        //put(WindowType.BEACON, new BlockInventoryTranslator(1, "minecraft:beacon", ContainerType.BEACON)); //TODO
    }


    @Event(filter = BedrockActionTranslator.class)
    public void onPlayerActionPacket(UpstreamPacketReceiveEvent<PlayerActionPacket> event) {
        Entity entity = event.getSession().getPlayerEntity();
        if (entity == null)
            return;

        switch (event.getPacket().getAction()) {
            case RESPAWN:
                RespawnPacket respawnPacket = new RespawnPacket();
                respawnPacket.setRuntimeEntityId(0);
                respawnPacket.setPosition(Vector3f.ZERO);
                respawnPacket.setState(RespawnPacket.State.SERVER_SEARCHING);
                event.getSession().sendUpstreamPacket(respawnPacket);

                ClientRequestPacket javaRespawnPacket = new ClientRequestPacket(ClientRequest.RESPAWN);
                event.getSession().sendDownstreamPacket(javaRespawnPacket);
                event.setCancelled(true);
        }
    }

    @Event(filter = RespawnPacket.class)
    public void onRespawnPacket(UpstreamPacketReceiveEvent<RespawnPacket> event) {
        // MCEE doesn't seem to provide this packet but we override it just in case
        event.setCancelled(true);
    }*/
}
