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
import java.util.List;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@Getter
public class BrewingStand extends MetaMapper {

    private final List<String> accepted = Arrays.asList(
            "minecraft:brewing_stand"
    );

    @Override
    public int getMeta(NbtMap originalState) {
        return (originalState.getBoolean("brewing_stand_slot_a_bit") ? 4 : 0)
                + (originalState.getBoolean("brewing_stand_slot_b_bit") ? 2 : 0)
                + (originalState.getBoolean("brewing_stand_slot_c_bit") ? 1 : 0);
    }
}
