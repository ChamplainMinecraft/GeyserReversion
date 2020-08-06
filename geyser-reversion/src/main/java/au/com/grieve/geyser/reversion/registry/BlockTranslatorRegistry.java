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

package au.com.grieve.geyser.reversion.registry;

import au.com.grieve.geyser.reversion.EduSupportPlugin;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.BaseBlockMapper;
import au.com.grieve.geyser.reversion.translators.blocks.remappers.BaseNbtRemapper;
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
public class BlockTranslatorRegistry {
    private final EduSupportPlugin plugin;

    private final Map<Integer, Integer> blockMap = new HashMap<>();
    private final List<BaseNbtRemapper> remappers = new ArrayList<>();
    private final List<BaseBlockMapper> mappers = new ArrayList<>();

    public BlockTranslatorRegistry(EduSupportPlugin plugin) {
        this.plugin = plugin;
    }

    public BlockTranslatorRegistry register(BaseBlockMapper mapper) {
        mappers.add(mapper);
        return this;
    }

    public BlockTranslatorRegistry register(BaseNbtRemapper mapper) {
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
    public BlockTranslatorRegistry execute() {
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
