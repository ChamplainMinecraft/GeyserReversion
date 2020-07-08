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

package au.com.grieve.edusupport.translators.blocks;

import au.com.grieve.edusupport.EduSupportPlugin;
import au.com.grieve.edusupport.translators.blocks.mappers.AgedBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Anvil;
import au.com.grieve.edusupport.translators.blocks.mappers.AxisBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Barrel;
import au.com.grieve.edusupport.translators.blocks.mappers.BaseBlockMapper;
import au.com.grieve.edusupport.translators.blocks.mappers.Bed;
import au.com.grieve.edusupport.translators.blocks.mappers.Bell;
import au.com.grieve.edusupport.translators.blocks.mappers.BrewingStand;
import au.com.grieve.edusupport.translators.blocks.mappers.BubbleColumn;
import au.com.grieve.edusupport.translators.blocks.mappers.Button;
import au.com.grieve.edusupport.translators.blocks.mappers.Cake;
import au.com.grieve.edusupport.translators.blocks.mappers.Cauldron;
import au.com.grieve.edusupport.translators.blocks.mappers.ChiseledBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.CobblestoneWall;
import au.com.grieve.edusupport.translators.blocks.mappers.Cocoa;
import au.com.grieve.edusupport.translators.blocks.mappers.ColoredBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.CommandBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Comparator;
import au.com.grieve.edusupport.translators.blocks.mappers.Composter;
import au.com.grieve.edusupport.translators.blocks.mappers.Coral;
import au.com.grieve.edusupport.translators.blocks.mappers.CoralBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.CoralFanHang;
import au.com.grieve.edusupport.translators.blocks.mappers.DirectionBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Dirt;
import au.com.grieve.edusupport.translators.blocks.mappers.Dispenser;
import au.com.grieve.edusupport.translators.blocks.mappers.Door;
import au.com.grieve.edusupport.translators.blocks.mappers.DoublePlant;
import au.com.grieve.edusupport.translators.blocks.mappers.Dropper;
import au.com.grieve.edusupport.translators.blocks.mappers.Elements;
import au.com.grieve.edusupport.translators.blocks.mappers.EndPortalFrame;
import au.com.grieve.edusupport.translators.blocks.mappers.FacingBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Farmland;
import au.com.grieve.edusupport.translators.blocks.mappers.Fence;
import au.com.grieve.edusupport.translators.blocks.mappers.FenceGate;
import au.com.grieve.edusupport.translators.blocks.mappers.Grindstone;
import au.com.grieve.edusupport.translators.blocks.mappers.GrowthBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.Hopper;
import au.com.grieve.edusupport.translators.blocks.mappers.InfestedBrick;
import au.com.grieve.edusupport.translators.blocks.mappers.Kelp;
import au.com.grieve.edusupport.translators.blocks.mappers.Lantern;
import au.com.grieve.edusupport.translators.blocks.mappers.Leaves;
import au.com.grieve.edusupport.translators.blocks.mappers.Lever;
import au.com.grieve.edusupport.translators.blocks.mappers.Liquid;
import au.com.grieve.edusupport.translators.blocks.mappers.Log;
import au.com.grieve.edusupport.translators.blocks.mappers.LoggerMapper;
import au.com.grieve.edusupport.translators.blocks.mappers.Mushroom;
import au.com.grieve.edusupport.translators.blocks.mappers.Observer;
import au.com.grieve.edusupport.translators.blocks.mappers.Planks;
import au.com.grieve.edusupport.translators.blocks.mappers.Portal;
import au.com.grieve.edusupport.translators.blocks.mappers.Prismarine;
import au.com.grieve.edusupport.translators.blocks.mappers.Rail;
import au.com.grieve.edusupport.translators.blocks.mappers.Rail2;
import au.com.grieve.edusupport.translators.blocks.mappers.Rail3;
import au.com.grieve.edusupport.translators.blocks.mappers.RedFlower;
import au.com.grieve.edusupport.translators.blocks.mappers.RedstoneSignal;
import au.com.grieve.edusupport.translators.blocks.mappers.Repeater;
import au.com.grieve.edusupport.translators.blocks.mappers.Sand;
import au.com.grieve.edusupport.translators.blocks.mappers.Sandstone;
import au.com.grieve.edusupport.translators.blocks.mappers.Sapling;
import au.com.grieve.edusupport.translators.blocks.mappers.Scaffolding;
import au.com.grieve.edusupport.translators.blocks.mappers.SeaPickle;
import au.com.grieve.edusupport.translators.blocks.mappers.Seagrass;
import au.com.grieve.edusupport.translators.blocks.mappers.SimpleMapper;
import au.com.grieve.edusupport.translators.blocks.mappers.SnowLayer;
import au.com.grieve.edusupport.translators.blocks.mappers.Sponge;
import au.com.grieve.edusupport.translators.blocks.mappers.Stairs;
import au.com.grieve.edusupport.translators.blocks.mappers.StandingSign;
import au.com.grieve.edusupport.translators.blocks.mappers.Stone;
import au.com.grieve.edusupport.translators.blocks.mappers.StoneBrick;
import au.com.grieve.edusupport.translators.blocks.mappers.StoneSlab;
import au.com.grieve.edusupport.translators.blocks.mappers.StoneSlab2;
import au.com.grieve.edusupport.translators.blocks.mappers.StoneSlab3;
import au.com.grieve.edusupport.translators.blocks.mappers.StoneSlab4;
import au.com.grieve.edusupport.translators.blocks.mappers.StrippedLog;
import au.com.grieve.edusupport.translators.blocks.mappers.StructureBlock;
import au.com.grieve.edusupport.translators.blocks.mappers.TallGrass;
import au.com.grieve.edusupport.translators.blocks.mappers.Tnt;
import au.com.grieve.edusupport.translators.blocks.mappers.Torch;
import au.com.grieve.edusupport.translators.blocks.mappers.Trapdoor;
import au.com.grieve.edusupport.translators.blocks.mappers.Tripwire;
import au.com.grieve.edusupport.translators.blocks.mappers.TripwireHook;
import au.com.grieve.edusupport.translators.blocks.mappers.TurtleEggs;
import au.com.grieve.edusupport.translators.blocks.mappers.Vine;
import au.com.grieve.edusupport.translators.blocks.mappers.Wood;
import au.com.grieve.edusupport.translators.blocks.mappers.WoodenSlab;
import au.com.grieve.edusupport.translators.blocks.remappers.Basalt;
import au.com.grieve.edusupport.translators.blocks.remappers.BaseNbtRemapper;
import au.com.grieve.edusupport.translators.blocks.remappers.SimpleRemapper;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtList;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.Getter;
import org.geysermc.connector.network.translators.world.block.BlockTranslator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Maps between Bedrock and MCEE Blocks
 */
@Getter
public class BlockMapper {
    private final EduSupportPlugin plugin;

    private final Map<Integer, Integer> blockMap = new HashMap<>();
    private final List<BaseNbtRemapper> remappers = new ArrayList<>();
    private final List<BaseBlockMapper> mappers = new ArrayList<>();

    public BlockMapper(EduSupportPlugin plugin) {
        this.plugin = plugin;

        register(new SimpleRemapper());
        register(new Basalt());


        register(new Stone());
        register(new Dirt());
        register(new Planks());
        register(new Sapling());
        register(new Liquid());
        register(new Sand());
        register(new Log());
        register(new StrippedLog());
        register(new Wood());
        register(new Leaves());
        register(new Sponge());
        register(new Dispenser());
        register(new Sandstone());
        register(new Bed());
        register(new Rail());
        register(new FacingBlock());
        register(new TallGrass());
        register(new Seagrass());
        register(new ColoredBlock());
        register(new RedFlower());
        register(new Tnt());
        register(new Torch());
        register(new AgedBlock());
        register(new Stairs());
        register(new RedstoneSignal());
        register(new GrowthBlock());
        register(new Farmland());
        register(new StandingSign());
        register(new Door());
        register(new Rail2());
        register(new Lever());
        register(new Button());
        register(new SnowLayer());
        register(new Fence());
        register(new Portal());
        register(new DirectionBlock());
        register(new Cake());
        register(new Repeater());
        register(new Trapdoor());
        register(new StoneBrick());
        register(new InfestedBrick());
        register(new Mushroom());
        register(new Vine());
        register(new FenceGate());
        register(new BrewingStand());
        register(new Cauldron());
        register(new EndPortalFrame());
        register(new Cocoa());
        register(new TripwireHook());
        register(new Tripwire());
        register(new CommandBlock());
        register(new CobblestoneWall());
        register(new Anvil());
        register(new Comparator());
        register(new Hopper());
        register(new ChiseledBlock());
        register(new Rail3());
        register(new Dropper());
        register(new Prismarine());
        register(new AxisBlock());
        register(new DoublePlant());
        register(new WoodenSlab());
        register(new StoneSlab());
        register(new StoneSlab2());
        register(new StoneSlab3());
        register(new StoneSlab4());
        register(new Observer());
        register(new Kelp());
        register(new TurtleEggs());
        register(new CoralBlock());
        register(new Coral());
        register(new CoralFanHang());
        register(new SeaPickle());
        register(new BubbleColumn());
        register(new Scaffolding());
        register(new Barrel());
        register(new Grindstone());
        register(new Bell());
        register(new Lantern());
        register(new StructureBlock());
        register(new Composter());
        register(new Elements());

        register(new SimpleMapper());
        register(new LoggerMapper());
        execute();
    }

    public BlockMapper register(BaseBlockMapper mapper) {
        mappers.add(mapper);
        return this;
    }

    public BlockMapper register(BaseNbtRemapper mapper) {
        remappers.add(mapper);
        return this;
    }

    public NbtList<NbtMap> getRuntimeBlockState() {
        try (NBTInputStream stream = NbtUtils.createNetworkReader(EduSupportPlugin.getInstance().getResourceAsStream("bedrock/runtime_block_states.dat"))) {
            //noinspection unchecked
            return (NbtList<NbtMap>) stream.readTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Perform a mapping of blocks
     */
    public BlockMapper execute() {
        NbtList<NbtMap> nbtList = getRuntimeBlockState();

        for (int from = 0; from < BlockTranslator.BLOCKS.size(); from++) {
            NbtMap original = BlockTranslator.BLOCKS.get(from);
            try {
                for (BaseNbtRemapper remapper : remappers) {
                    original = remapper.map(original);
                }

                NbtMap finalOriginal = original;
                int to = mappers.stream()
                        .map(m -> m.map(nbtList, finalOriginal))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(0);
                blockMap.put(from, to);
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().error("Failed to map: " + original);
            }
        }

        // Free Mappers as we don't need them anymore
        mappers.clear();

        // Patch BlockTranslator
        try {
            final Field btBlocks = BlockTranslator.class.getDeclaredField("BLOCKS");
            btBlocks.setAccessible(true);

            // remove final modifier from field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(btBlocks, btBlocks.getModifiers() & ~Modifier.FINAL);
            btBlocks.set(null, nbtList);

            final Field btJavaToBedrockBlockMap = BlockTranslator.class.getDeclaredField("JAVA_TO_BEDROCK_BLOCK_MAP");
            btJavaToBedrockBlockMap.setAccessible(true);
            Int2IntMap javaToBedrockBlockMap = ((Int2IntMap) btJavaToBedrockBlockMap.get(null));

            for (int i : javaToBedrockBlockMap.keySet()) {
                int to = blockMap.get(javaToBedrockBlockMap.get(i));
                javaToBedrockBlockMap.put(i, to);
            }

            final Field btBedrockToJavaBlockMap = BlockTranslator.class.getDeclaredField("BEDROCK_TO_JAVA_BLOCK_MAP");
            btBedrockToJavaBlockMap.setAccessible(true);
            Int2IntMap bedrockToJavaBlockMap = ((Int2IntMap) btBedrockToJavaBlockMap.get(null));

            Int2IntMap newMap = new Int2IntOpenHashMap();
            for (Int2IntMap.Entry entry : bedrockToJavaBlockMap.int2IntEntrySet()) {
                int to = blockMap.getOrDefault(entry.getIntKey(), entry.getIntKey());
                newMap.put(to, entry.getIntValue());
            }
            bedrockToJavaBlockMap.clear();
            bedrockToJavaBlockMap.putAll(newMap);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return this;
    }

}
