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
public class FacingBlock extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:sticky_piston",
            "minecraft:piston",
            "minecraft:pistonArmCollision",
            "minecraft:chest",
            "minecraft:furnace",
            "minecraft:lit_furnace",
            "minecraft:ladder",
            "minecraft:wall_sign",
            "minecraft:spruce_wall_sign",
            "minecraft:birch_wall_sign",
            "minecraft:jungle_wall_sign",
            "minecraft:acacia_wall_sign",
            "minecraft:darkoak_wall_sign",
            "minecraft:white_glazed_terracotta",
            "minecraft:orange_glazed_terracotta",
            "minecraft:magenta_glazed_terracotta",
            "minecraft:light_blue_glazed_terracotta",
            "minecraft:yellow_glazed_terracotta",
            "minecraft:lime_glazed_terracotta",
            "minecraft:pink_glazed_terracotta",
            "minecraft:gray_glazed_terracotta",
            "minecraft:silver_glazed_terracotta",
            "minecraft:cyan_glazed_terracotta",
            "minecraft:purple_glazed_terracotta",
            "minecraft:blue_glazed_terracotta",
            "minecraft:brown_glazed_terracotta",
            "minecraft:green_glazed_terracotta",
            "minecraft:red_glazed_terracotta",
            "minecraft:black_glazed_terracotta",
            "minecraft:ender_chest",
            "minecraft:skull",
            "minecraft:trapped_chest",
            "minecraft:wall_banner",
            "minecraft:end_rod",
            "minecraft:lit_smoker",
            "minecraft:smoker",
            "minecraft:lit_blast_furnace",
            "minecraft:blast_furnace",
            "minecraft:stonecutter_block",
            "minecraft:jigsaw",
            "minecraft:frame"

    );

    @Override
    public int getMeta(NbtMap originalState) {
        return originalState.getInt("facing_direction");
    }
}
