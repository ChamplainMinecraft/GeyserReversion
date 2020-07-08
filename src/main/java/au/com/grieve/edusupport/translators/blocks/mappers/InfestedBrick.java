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

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@Getter
public class InfestedBrick extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:monster_egg"
    );

    private final Map<String, Integer> material = new HashMap<>() {{
        put("stone", 0);
        put("cobblestone", 1);
        put("stone_brick", 2);
        put("mossy_stone_brick", 3);
        put("cracked_stone_brick", 4);
        put("chiseled_stone_brick", 5);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return material.getOrDefault(originalState.getString("monster_egg_stone_type"), 0);
    }
}
