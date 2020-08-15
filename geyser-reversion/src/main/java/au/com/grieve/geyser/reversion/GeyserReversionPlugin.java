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

import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.config.Configuration;
import au.com.grieve.geyser.reversion.editions.bedrock.BedrockEdition;
import au.com.grieve.geyser.reversion.editions.education.EducationEdition;
import au.com.grieve.reversion.api.Translator;
import au.com.grieve.reversion.translators.v390ee_to_v408be.Translator_v390ee_to_v408be;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.event.handlers.EventHandler;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Plugin(
        name = "GeyserReversion",
        version = "1.1.0-dev",
        authors = {"bundabrg"},
        description = "Provides multiversion protocol support for Geyser"
)
@Getter
public class GeyserReversionPlugin extends GeyserPlugin {
    @Getter
    private static GeyserReversionPlugin instance;

    private final Map<String, Edition> registeredEditions = new HashMap<>();
    private final List<Class<? extends Translator>> registeredTranslators = new ArrayList<>();

    private Configuration config;

    public GeyserReversionPlugin(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);
        instance = this;

        loadConfig();
        registerEditions();
        registerTranslators();
    }

    /**
     * Register built-in editions
     */
    private void registerEditions() {
        registerEdition("bedrock", new BedrockEdition(this));
        registerEdition("education", new EducationEdition(this));
    }

    /**
     * Register built-in translators
     */
    private void registerTranslators() {
        registerTranslator(Translator_v390ee_to_v408be.class);
    }


    /**
     * Register an Edition
     */
    public void registerEdition(String name, Edition edition) {
        registeredEditions.put(name, edition);
        getLogger().debug("Registered Edition: " + name);
    }

    /**
     * Register a Translator
     */
    public void registerTranslator(Class<? extends Translator> translatorClass) {
        registeredTranslators.add(translatorClass);
        getLogger().debug("Registered Translator: " + translatorClass.getSimpleName());
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
     * Replace Geyser BedrockServer with one provided by an edition
     */
    @GeyserEventHandler(priority = EventHandler.PRIORITY.HIGH)
    public void onGeyserStart(GeyserStartEvent event) {
        Edition edition = registeredEditions.get(config.getEdition());

        if (edition == null) {
            getLogger().error(String.format("Invalid Edition '%s'. Plugin disabled.", config.getEdition()));
            return;
        }

        try {
            Field bedrockServer = GeyserConnector.class.getDeclaredField("bedrockServer");
            bedrockServer.setAccessible(true);
            bedrockServer.set(GeyserConnector.getInstance(), edition.createReversionServer(GeyserConnector.getInstance().getBedrockServer().getBindAddress()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().error(String.format("Unable to set Edition '%s'. Plugin disabled.", config.getEdition()), e);
        }
    }
}
