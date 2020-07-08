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
public class Door extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:wooden_door",
            "minecraft:iron_door",
            "minecraft:spruce_door",
            "minecraft:birch_door",
            "minecraft:jungle_door",
            "minecraft:acacia_door",
            "minecraft:dark_oak_door"
    );

    private final Map<Boolean, Integer> hinge = new HashMap<>() {{
        put(false, 0);
        put(true, 1);
    }};

    private final Map<Boolean, Integer> open = new HashMap<>() {{
        put(false, 0);
        put(true, 4);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        if (originalState.getBoolean("upper_block_bit")) {
            return 8
                    + hinge.get(originalState.getBoolean("door_hinge_bit"));
        }

        return originalState.getInt("direction")
                + open.get(originalState.getBoolean("open_bit"));
    }
}
