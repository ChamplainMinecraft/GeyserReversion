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
public class StoneBrick extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:stonebrick"
    );

    private final Map<String, Integer> material = new HashMap<>() {{
        put("default", 0);
        put("mossy", 1);
        put("cracked", 2);
        put("chiseled", 3);
        put("smooth", 4);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return material.get(originalState.getString("stone_brick_type"));
    }
}
