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

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@Getter
public class DoublePlant extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:double_plant"
    );

    private final Map<String, Integer> type = new HashMap<>() {{
        put("sunflower", 0);
        put("syringa", 1);
        put("grass", 2);
        put("fern", 3);
        put("rose", 4);
        put("paeonia", 5);
    }};

    private final Map<Boolean, Integer> upper = new HashMap<>() {{
        put(false, 0);
        put(true, 8);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return type.get(originalState.getString("double_plant_type"))
                + upper.get(originalState.getBoolean("upper_block_bit"));
    }
}
