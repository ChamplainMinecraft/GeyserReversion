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
public class CoralFanHang extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:coral_fan_hang",
            "minecraft:coral_fan_hang2",
            "minecraft:coral_fan_hang3"
    );

    private final Map<Boolean, Integer> type = new HashMap<>() {{
        put(false, 0);
        put(true, 1);
    }};

    private final Map<Boolean, Integer> dead = new HashMap<>() {{
        put(false, 0);
        put(true, 2);
    }};

    private final Map<Integer, Integer> direction = new HashMap<>() {{
        put(0, 0);
        put(1, 4);
        put(2, 8);
        put(3, 12);

    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return type.get(originalState.getBoolean("coral_hang_type_bit"))
                + dead.get(originalState.getBoolean("dead_bit"))
                + direction.get(originalState.getInt("coral_direction"));
    }
}
