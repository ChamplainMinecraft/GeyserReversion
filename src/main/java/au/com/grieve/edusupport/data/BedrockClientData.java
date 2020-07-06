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

package au.com.grieve.edusupport.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.minidev.json.JSONValue;

import java.util.Base64;

@Getter
public class BedrockClientData extends org.geysermc.connector.network.session.auth.BedrockClientData {
    @JsonProperty(value = "SkinGeometryName")
    private String geometryName;
    @JsonProperty(value = "SkinGeometry")
    private String geometryData;

    public String getGeometryName() {
        return Base64.getEncoder().encodeToString(("{\"geometry\" : {\"default\" : \"" + JSONValue.escape(new String(geometryName.getBytes())) + "\"}}").getBytes());
    }
}
