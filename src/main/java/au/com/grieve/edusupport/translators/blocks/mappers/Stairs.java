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

package au.com.grieve.edusupport.translators.blocks.mappers;

import com.nukkitx.nbt.NbtMap;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Stairs extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:oak_stairs",
            "minecraft:spruce_stairs",
            "minecraft:birch_stairs",
            "minecraft:jungle_stairs",
            "minecraft:acacia_stairs",
            "minecraft:dark_oak_stairs",
            "minecraft:stone_stairs",
            "minecraft:brick_stairs",
            "minecraft:stone_brick_stairs",
            "minecraft:nether_brick_stairs",
            "minecraft:sandstone_stairs",
            "minecraft:quartz_stairs",
            "minecraft:prismarine_stairs",
            "minecraft:prismarine_bricks_stairs",
            "minecraft:dark_prismarine_stairs",
            "minecraft:red_sandstone_stairs",
            "minecraft:purpur_stairs",
            "minecraft:polished_granite_stairs",
            "minecraft:smooth_red_sandstone_stairs",
            "minecraft:mossy_stone_brick_stairs",
            "minecraft:polished_diorite_stairs",
            "minecraft:mossy_cobblestone_stairs",
            "minecraft:end_brick_stairs",
            "minecraft:normal_stone_stairs",
            "minecraft:smooth_sandstone_stairs",
            "minecraft:smooth_quartz_stairs",
            "minecraft:granite_stairs",
            "minecraft:andesite_stairs",
            "minecraft:red_nether_brick_stairs",
            "minecraft:polished_andesite_stairs",
            "minecraft:diorite_stairs"


    );

    private final Map<Boolean, Integer> upsideDown = new HashMap<>() {{
        put(false, 0);
        put(true, 4);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return originalState.getInt("weirdo_direction")
                + upsideDown.get(originalState.getBoolean("upside_down_bit"));
    }
}
