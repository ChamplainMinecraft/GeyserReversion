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

package au.com.grieve.geyser.reversion.translators.blocks.mappers;

import com.nukkitx.nbt.NbtMap;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class SimpleMapper extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:air",
            "minecraft:stone",
            "minecraft:grass",
            "minecraft:podzol",
            "minecraft:cobblestone",
            "minecraft:bedrock",
            "minecraft:gravel",
            "minecraft:iron_ore",
            "minecraft:gold_ore",
            "minecraft:coal_ore",
            "minecraft:glass",
            "minecraft:lapis_ore",
            "minecraft:lapis_block",
            "minecraft:noteblock",
            "minecraft:web",
            "minecraft:deadbush",
            "minecraft:movingBlock",
            "minecraft:yellow_flower",
            "minecraft:brown_mushroom",
            "minecraft:red_mushroom",
            "minecraft:gold_block",
            "minecraft:iron_block",
            "minecraft:brick_block",

            "minecraft:bookshelf",
            "minecraft:mossy_cobblestone",
            "minecraft:obsidian",
            "minecraft:mob_spawner",
            "minecraft:diamond_ore",
            "minecraft:diamond_block",
            "minecraft:crafting_table",
            "minecraft:lit_redstone_ore",
            "minecraft:redstone_ore",
            "minecraft:ice",
            "minecraft:snow",
            "minecraft:clay",
            "minecraft:jukebox",
            "minecraft:pumpkin",
            "minecraft:netherrack",
            "minecraft:soul_sand",
            "minecraft:glowstone",
            "minecraft:iron_bars",
            "minecraft:glass_pane",
            "minecraft:melon_block",
            "minecraft:enchanting_table",
            "minecraft:mycelium",
            "minecraft:waterlily",
            "minecraft:nether_brick",
            "minecraft:end_portal",
            "minecraft:nether_brick_fence",
            "minecraft:end_stone",
            "minecraft:dragon_egg",
            "minecraft:lit_redstone_lamp",
            "minecraft:redstone_lamp",
            "minecraft:emerald_ore",
            "minecraft:emerald_block",
            "minecraft:beacon",
            "minecraft:flower_pot",
            "minecraft:redstone_block",
            "minecraft:quartz_ore",
            "minecraft:slime",
            "minecraft:barrier",
            "minecraft:seaLantern",
            "minecraft:hardened_clay",
            "minecraft:coal_block",
            "minecraft:packed_ice",
            "minecraft:smooth_stone",
            "minecraft:chorus_plant",
            "minecraft:end_bricks",
            "minecraft:grass_path",
            "minecraft:end_gateway",
            "minecraft:magma",
            "minecraft:nether_wart_block",
            "minecraft:red_nether_brick",
            "minecraft:undyed_shulker_box",
            "minecraft:dried_kelp_block",
            "minecraft:blue_ice",
            "minecraft:conduit",
            "minecraft:bamboo_sapling",
            "minecraft:bamboo",
            "minecraft:flower_pot",
            "minecraft:cartography_table",
            "minecraft:fletching_table",
            "minecraft:smithing_table",
            "minecraft:hard_glass",
            "minecraft:border_block",
            "minecraft:info_update",
            "minecraft:invisibleBedrock",
            "minecraft:netherreactor",
            "minecraft:reserved6",
            "minecraft:deny",
            "minecraft:camera",
            "minecraft:glowingobsidian",
            "minecraft:chemical_heat",
            "minecraft:hard_glass_pane",
            "minecraft:allow",
            "minecraft:stonecutter",
            "minecraft:info_update2"


    );

    @Override
    public int getMeta(NbtMap originalState) {
        return 0;
    }
}
