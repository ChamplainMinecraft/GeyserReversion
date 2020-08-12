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
import au.com.grieve.geyser.reversion.api.BaseTranslator;
import au.com.grieve.geyser.reversion.api.TranslatorException;
import au.com.grieve.geyser.reversion.server.ReversionServer;
import lombok.Getter;
import lombok.Value;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.network.NewBedrockServerEvent;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.plugin.GeyserPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ReversionManager {
    @Getter
    private static ReversionManager instance;

    private final Map<String, BaseEdition> registeredEditions = new HashMap<>();
    private final List<RegisteredTranslator> registeredTranslators = new ArrayList<>();

    private final GeyserPlugin plugin;
    private final String editionName;

    public ReversionManager(GeyserPlugin plugin, String editionName) {
        instance = this;
        this.plugin = plugin;
        this.editionName = editionName;

        plugin.registerEvents(this);
    }

    public void registerEdition(String name, BaseEdition edition) {
        registeredEditions.put(name, edition);
    }

    public void registerTranslator(String fromEdition, int fromProtocolVersion, String toEdition,
                                   int toProtocolVersion, Class<? extends BaseTranslator> translatorClass) {
        registeredTranslators.add(
                new RegisteredTranslator(fromEdition, fromProtocolVersion, toEdition, toProtocolVersion, translatorClass)
        );
    }

    /**
     * Grab the default Geyser Server and wrap it here
     *
     * @param event the event
     */
    @GeyserEventHandler
    public void onNewBedrockServer(NewBedrockServerEvent event) {
        BaseEdition edition = registeredEditions.get(editionName);
        if (edition == null) {
            plugin.getLogger().error("Unknown Edition '" + editionName + "'");
            return;
        }

        ReversionServer server = new ReversionServer(event.getBedrockServer());
        edition.getServerEventHandler().setOriginal(event.getBedrockServer().getHandler());
        server.setHandler(edition.getServerEventHandler());

        event.setBedrockServer(new ReversionServer(event.getBedrockServer()));
    }

    /**
     * Create a translator chain from the client to the server.
     *
     * @param fromProtocolVersion Version of protocol used by client
     * @param session             the geyser session
     * @return first translator in chain
     * @throws TranslatorException on error
     */
    public BaseTranslator createTranslatorChain(int fromProtocolVersion, GeyserSession session) throws TranslatorException {
        List<RegisteredTranslator> bestChain = getBestTranslatorChain(editionName, fromProtocolVersion,
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
