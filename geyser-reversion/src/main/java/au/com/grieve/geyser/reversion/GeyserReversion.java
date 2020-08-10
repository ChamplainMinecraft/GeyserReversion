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

package au.com.grieve.geyser.reversion;

import au.com.grieve.geyser.reversion.api.BaseEdition;
import au.com.grieve.geyser.reversion.config.Configuration;
import au.com.grieve.geyser.reversion.editions.mcee.EducationEdition;
import lombok.Getter;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.network.NewBedrockServerEvent;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Plugin(
        name = "GeyserReversion",
        version = "1.1.0-dev",
        authors = {"bundabrg"},
        description = "Provides multiversion protocol support for Geyser"
)
@Getter
public class GeyserReversion extends GeyserPlugin {
    @Getter
    private static GeyserReversion instance;

    public final Map<String, BaseEdition> editions = new HashMap<>();

    private Configuration config;


    public GeyserReversion(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);
        instance = this;

        loadConfig();

        // Register Editions
        registerEdition("education", new EducationEdition(this));
//        registerServerHandler("mcpe", new McpeServerHandler());
//        registerServerHandler("mcee", new MceeServerHandler(this));
    }

    /**
     * Load our config, generating it if necessary
     */
    private void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(configFile);
                 InputStream fis = getResourceAsStream("config.yml")) {
                fis.transferTo(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = Configuration.loadFromFile(configFile);
    }

    /**
     * Grab the default Geyser Server and wrap it here
     *
     * @param event the event
     */
    @GeyserEventHandler
    public void onNewBedrockServer(NewBedrockServerEvent event) {
        BaseEdition edition = editions.get(config.getEdition());
        if (edition == null) {
            getLogger().error("Unknown Edition: " + config.getEdition());
            return;
        }

        event.setBedrockServer(edition.hook(event.getBedrockServer()));
    }

    public void registerEdition(String name, BaseEdition edition) {
        editions.put(name, edition);
    }

}
