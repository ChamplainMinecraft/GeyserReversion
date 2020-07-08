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

import com.nukkitx.nbt.NbtList;
import com.nukkitx.nbt.NbtMap;

import java.util.List;

public abstract class MetaMapper extends BaseBlockMapper {

    public abstract List<String> getAccepted();

    public abstract int getMeta(NbtMap originalState);

    @Override
    public Integer map(NbtList<NbtMap> runtimeBlockStates, NbtMap original) {
        String originalName = original.getCompound("block").getString("name");
        if (!getAccepted().contains(originalName)) {
            return null;
        }

        NbtMap originalState = original.getCompound("block").getCompound("states");
        int meta = getMeta(originalState);

        for (int i = 0; i < runtimeBlockStates.size(); i++) {
            NbtMap block = runtimeBlockStates.get(i);
            if (block.getCompound("block").getString("name").equals(originalName)
                    && block.getShort("meta") == meta) {
                return i;
            }
        }
        return null;
    }
}
