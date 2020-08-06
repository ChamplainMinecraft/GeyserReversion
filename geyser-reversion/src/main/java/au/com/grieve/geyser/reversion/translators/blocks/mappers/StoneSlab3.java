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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StoneSlab3 extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:stone_slab3",
            "minecraft:double_stone_slab3"
    );

    private final Map<String, Integer> type = new HashMap<>() {{
        put("end_stone_brick", 0);
        put("smooth_red_sandstone", 1);
        put("polished_andesite", 2);
        put("andesite", 3);
        put("diorite", 4);
        put("polished_diorite", 5);
        put("granite", 6);
        put("polished_granite", 7);
    }};

    private final Map<Boolean, Integer> top = new HashMap<>() {{
        put(false, 0);
        put(true, 8);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return type.get(originalState.getString("stone_slab_type_3"))
                + top.get(originalState.getBoolean("top_slot_bit"));
    }
}
