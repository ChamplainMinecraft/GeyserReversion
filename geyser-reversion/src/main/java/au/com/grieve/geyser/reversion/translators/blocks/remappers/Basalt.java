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

package au.com.grieve.geyser.reversion.translators.blocks.remappers;

import com.nukkitx.nbt.NbtMap;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Basalt extends BaseNbtRemapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:basalt",
            "minecraft:polished_basalt"
    );

    private final Map<String, Integer> pillar = new HashMap<>() {{
        put("x", 2);
        put("y", 3);
        put("z", 1);
    }};

    @Override
    public NbtMap map(NbtMap original) {
        String originalName = original.getCompound("block").getString("name");
        if (!getAccepted().contains(originalName)) {
            return original;
        }

        NbtMap originalStates = original.getCompound("block").getCompound("states");

        return original.toBuilder()
                .putCompound("block", original.getCompound("block").toBuilder()
                        .putString("name", "minecraft:gray_glazed_terracotta")
                        .putCompound("states", NbtMap.builder()
                                .putInt("facing_direction", pillar.get(originalStates.getString("pillar_axis")))
                                .build()
                        )
                        .build())
                .build();
    }
}
