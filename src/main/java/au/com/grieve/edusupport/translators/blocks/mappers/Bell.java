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
public class Bell extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:bell"
    );

    private final Map<String, Integer> attachment = new HashMap<>() {{
        put("standing", 0);
        put("hanging", 4);
        put("side", 8);
        put("multiple", 12);
    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return originalState.getInt("direction")
                + attachment.get(originalState.getString("attachment"));
    }
}
