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
public class Sandstone extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:sandstone",
            "minecraft:red_sandstone"
    );

    private final Map<String, Integer> type = new HashMap<>() {{
        put("default", 0);
        put("heiroglyphs", 1);
        put("cut", 2);
        put("smooth", 3);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return type.get(originalState.getString("sand_stone_type"));
    }
}
