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

import au.com.grieve.geyser.reversion.api.BaseTranslator;
import au.com.grieve.geyser.reversion.api.TranslatorException;
import au.com.grieve.geyser.reversion.config.Configuration;
import au.com.grieve.geyser.reversion.editions.mcee.commands.EducationCommand;
import au.com.grieve.geyser.reversion.editions.mcee.utils.TokenManager;
import au.com.grieve.geyser.reversion.server.ReversionServer;
import lombok.Getter;
import lombok.Value;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.event.events.network.NewBedrockServerEvent;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public final List<RegisteredTranslator> registeredTranslators = new ArrayList<>();

    private final TokenManager tokenManager;
    private Configuration config;

    public GeyserReversion(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);
        instance = this;
        tokenManager = new TokenManager(this);

        loadConfig();
        registerTranslators();
    }

    /**
     * Register Default translators
     */
    protected void registerTranslators() {

    }

    public void registerTranslator(String fromEdition, int fromProtocolVersion, String toEdition,
                                   int toProtocolVersion, Class<? extends BaseTranslator> translatorClass) {
        registeredTranslators.add(
                new RegisteredTranslator(fromEdition, fromProtocolVersion, toEdition, toProtocolVersion, translatorClass)
        );
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
        ReversionServer server = new ReversionServer(event.getBedrockServer());
        server.setHandler();

        event.setBedrockServer(new ReversionServer(event.getBedrockServer()));
    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        GeyserConnector.getInstance().getBootstrap().getGeyserCommandManager().registerCommand(
                new EducationCommand("education", "Education Commands", "geyser.command.education", tokenManager));
    }
//    @Getter
//    @RequiredArgsConstructor
//    public class VersionDetectPacketHandler implements BedrockPacketHandler {
//
//        @Override
//        public boolean handle(LoginPacket packet) {
//            try {
//                translator = GeyserReversion.getInstance().createTranslatorChain(packet.getProtocolVersion(), geyserSession);
//                EducationEdition.getInstance().getPlugin().getLogger().debug("Player connected with version: " + translator.getCodec().getMinecraftVersion());
//                setPacketCodec(translator.getCodec());
//                return false;
//            } catch (TranslatorException e) {
//                EducationEdition.getInstance().getPlugin().getLogger().error("Failed to load Version Translation", e);
//            }
//            return false;
//        }
//    }


    /**
     * Create a translator chain from the client to the server.
     * <p>
     * The edition is inferred
     *
     * @param fromProtocolVersion Version of protocol used by client
     * @param session             the geyser session
     * @return first translator in chain
     * @throws TranslatorException on error
     */
    public BaseTranslator createTranslatorChain(int fromProtocolVersion, GeyserSession session) throws TranslatorException {
        List<RegisteredTranslator> bestChain = getBestTranslatorChain(config.getEdition(), fromProtocolVersion,
                "bedrock", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion(), new ArrayList<>(registeredTranslators));

        if (bestChain == null) {
            // No translator found so return the default
            try {
                return BaseTranslator.DefaultTranslator.class.getConstructor(GeyserSession.class).newInstance(session);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new TranslatorException(e);
            }
        }

        BaseTranslator ret = null;
        BaseTranslator current = null;
        for (RegisteredTranslator registeredTranslator : bestChain) {
            BaseTranslator translator;
            try {
                translator = registeredTranslator.getTranslatorClass().getConstructor(GeyserPlugin.class, GeyserSession.class)
                        .newInstance(this, session);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new TranslatorException(e);
            }

            if (current != null) {
                translator.setPrevious(current);
                current.setNext(translator);
            }

            if (ret == null) {
                ret = translator;
            }

            current = translator;
        }

        return ret;
    }

    /**
     * Return shortest chain from options available in registeredTranslators
     *
     * @param fromEdition           from edition
     * @param fromProtocolVersion   from protocol version
     * @param toEdition             to edition
     * @param toProtocolVersion     to protocol version
     * @param registeredTranslators registered translators
     * @return list of Registered translators satisfying condition else null
     */
    private List<RegisteredTranslator> getBestTranslatorChain(String fromEdition, int fromProtocolVersion, String toEdition, int toProtocolVersion, List<RegisteredTranslator> registeredTranslators) {
        List<RegisteredTranslator> best = null;
        for (RegisteredTranslator translator : registeredTranslators) {
            if (!translator.fromEdition.equals(fromEdition) || translator.fromProtocolVersion != fromProtocolVersion) {
                continue;
            }

            // Found a solution
            if (translator.toEdition.equals(toEdition) && translator.toProtocolVersion == toProtocolVersion) {
                return Collections.singletonList(translator);
            }

            // Find shortest
            List<RegisteredTranslator> newRegisteredTranslators = registeredTranslators.stream().filter(t -> t != translator).collect(Collectors.toList());
            List<RegisteredTranslator> current = getBestTranslatorChain(translator.toEdition, translator.toProtocolVersion, toEdition, toProtocolVersion, newRegisteredTranslators);

            if (current == null) {
                continue;
            }

            if (best == null || best.size() > current.size() + 1) {
                best = new ArrayList<>();
                best.add(translator);
                best.addAll(current);
            }
        }
        return best;
    }

    @Value
    public static class RegisteredTranslator {
        String fromEdition;
        int fromProtocolVersion;
        String toEdition;
        int toProtocolVersion;
        Class<? extends BaseTranslator> translatorClass;
    }

}
