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
public class Lever extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:lever"
    );

    private final Map<Boolean, Integer> open = new HashMap<>() {{
        put(false, 0);
        put(true, 8);
    }};

    private final Map<String, Integer> direction = new HashMap<>() {{
        put("down_east_west", 0);
        put("east", 1);
        put("west", 2);
        put("south", 3);
        put("north", 4);
        put("up_north_south", 5);
        put("up_east_west", 6);
        put("down_north_south", 7);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return direction.get(originalState.getString("lever_direction"))
                + open.get(originalState.getBoolean("open_bit"));
    }
}
