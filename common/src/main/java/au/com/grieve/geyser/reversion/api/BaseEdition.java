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

package au.com.grieve.geyser.reversion.api;

import au.com.grieve.geyser.reversion.server.ReversionServerEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.plugin.GeyserPlugin;

@Getter
@RequiredArgsConstructor
public abstract class BaseEdition {
    private final GeyserPlugin plugin;

    public abstract ReversionServerEventHandler getServerEventHandler();
}
