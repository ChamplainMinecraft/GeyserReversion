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

import au.com.grieve.edusupport.commands.EducationCommand;
import au.com.grieve.edusupport.packets.UpstreamPackets;
import au.com.grieve.edusupport.translators.blocks.BlockMapper;
import au.com.grieve.edusupport.utils.TokenManager;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.v363.Bedrock_v363;
import lombok.Getter;
import org.geysermc.connector.event.annotations.Event;
import org.geysermc.connector.event.events.BedrockCodecRegistryEvent;
import org.geysermc.connector.event.events.BedrockPongEvent;
import org.geysermc.connector.event.events.GeyserStartEvent;
import org.geysermc.connector.event.events.PluginDisableEvent;
import org.geysermc.connector.event.events.UpstreamPacketSendEvent;
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
    public static EduSupportPlugin instance;

    private final TokenManager tokenManager;

    public EduSupportPlugin(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);

        instance = this;
        tokenManager = new TokenManager();
        registerEvents(new UpstreamPackets());
    }

    @Event
    public void onCodecRegistry(BedrockCodecRegistryEvent event) {
        event.setCodec(Bedrock_v363.V363_CODEC);
    }

    @Event
    public void onBedrockPong(BedrockPongEvent event) {
        event.getPong().setEdition("MCEE");
    }


//    @Event
//    public void onResourceRead(ResourceReadEvent event) {
//        // See if resource exists in our own folder and load it instead
//        InputStream stream = EduSupportPlugin.getInstance().getResourceAsStream(event.getResourceName());
//        if (stream != null) {
//            event.setInputStream(stream);
//        }
//    }


//    @Event
//    public void onBuildBlockState(BuildBlockStateMapEvent event) {
//        /* Load block palette */
//        InputStream stream = FileUtils.getResource("bedrock/runtime_block_states.dat");
//
//        NbtList<NbtMap> blocksTag;
//        try (NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
//            blocksTag = EventManager.getInstance().triggerEvent(new RuntimeBlockStateReadEvent(
//                    (NbtList<NbtMap>) nbtInputStream.readTag())).getEvent().getBlockStates();
//        } catch (Exception e) {
//            throw new AssertionError("Unable to get blocks from runtime block states", e);
//        }
//
//        for (NbtMap tag : blocksTag) {
//            // Fake up a block that includes meta
//            NbtMapBuilder tagBuilder = tag.toBuilder();
//            NbtMapBuilder blockBuilder = tag.getCompound("block").toBuilder();
//            blockBuilder.putShort("meta", tag.getShort("meta", (short) 0));
//            tagBuilder.put("block", blockBuilder.build());
//
//            if (event.getBlockStateMap().putIfAbsent(blockBuilder.build(), tag) != null) {
//                throw new AssertionError("Duplicate block states in Bedrock palette");
//            }
//        }
//        event.setCancelled(true);
//    }
//
//    @Event
//    public void onBuildBedrockState(BuildBedrockStateEvent event) {
//        NbtMapBuilder tagBuilder = NbtMap.builder();
//        tagBuilder.putString("name", event.getBlockNode().get("bedrock_identifier").textValue());
//
//        short meta = event.getBlockNode().has("meta") ? event.getBlockNode().get("meta").shortValue() : 0;
//        tagBuilder.putShort("meta", meta);
//
//        event.setBlockState(tagBuilder.build());
//    }

    @Event
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        getConnector().getBootstrap().getGeyserCommandManager().registerCommand(new EducationCommand(getConnector(), "education", "Education Commands", "geyser.command.education", tokenManager));

        // Register BlockMappings
        BlockMapper blockMapperManager = new BlockMapper(this);


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

    @Event
    public void onDisable(PluginDisableEvent event) {
        System.err.println("I'm dead");
    }

    @Event
    public void onUpstreamSend(UpstreamPacketSendEvent<BedrockPacket> event) {
        getLogger().info("Sending: " + event.getPacket());
    }
}
