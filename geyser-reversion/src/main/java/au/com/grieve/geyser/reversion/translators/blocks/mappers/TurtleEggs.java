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
public class TurtleEggs extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:turtle_egg"
    );

    private final Map<String, Integer> count = new HashMap<>() {{
        put("one_egg", 0);
        put("two_egg", 1);
        put("three_egg", 2);
        put("four_egg", 3);
    }};

    private final Map<String, Integer> cracked = new HashMap<>() {{
        put("no_cracks", 0);
        put("cracked", 4);
        put("max_cracked", 8);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return count.get(originalState.getString("turtle_egg_count"))
                + cracked.get(originalState.getString("cracked_state"));
    }
}
