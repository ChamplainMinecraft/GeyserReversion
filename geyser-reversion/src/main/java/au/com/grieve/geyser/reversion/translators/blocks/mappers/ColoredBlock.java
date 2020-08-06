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
public class ColoredBlock extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:wool",
            "minecraft:stained_glass",
            "minecraft:stained_hardened_clay",
            "minecraft:stained_glass_pane",
            "minecraft:carpet",
            "minecraft:shulker_box",
            "minecraft:concrete",
            "minecraft:concretePowder",
            "minecraft:hard_stained_glass",
            "minecraft:hard_stained_glass_pane"
    );

    private final Map<String, Integer> color = new HashMap<>() {{
        put("white", 0);
        put("orange", 1);
        put("magenta", 2);
        put("light_blue", 3);
        put("yellow", 4);
        put("lime", 5);
        put("pink", 6);
        put("gray", 7);
        put("silver", 8);
        put("cyan", 9);
        put("purple", 10);
        put("blue", 11);
        put("brown", 12);
        put("green", 13);
        put("red", 14);
        put("black", 15);

    }};

    @Override
    public int getMeta(NbtMap originalState) {
        return color.get(originalState.getString("color"));
    }
}
