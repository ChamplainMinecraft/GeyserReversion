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
public class RedFlower extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:red_flower"
    );

    private final Map<String, Integer> type = new HashMap<>() {{
        put("poppy", 0);
        put("orchid", 1);
        put("allium", 2);
        put("houstonia", 3);
        put("tulip_red", 4);
        put("tulip_orange", 5);
        put("tulip_white", 6);
        put("tulip_pink", 7);
        put("oxeye", 8);
        put("cornflower", 9);
        put("lily_of_the_valley", 10);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return type.get(originalState.getString("flower_type"));
    }
}
