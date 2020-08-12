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

package au.com.grieve.geyser.reversion.editions.mcee;

import au.com.grieve.geyser.reversion.api.BaseEdition;
import au.com.grieve.geyser.reversion.api.BaseTranslator;
import au.com.grieve.geyser.reversion.api.TranslatorException;
import au.com.grieve.geyser.reversion.editions.mcee.commands.EducationCommand;
import au.com.grieve.geyser.reversion.editions.mcee.hook.ReversionBedrockServer;
import au.com.grieve.geyser.reversion.editions.mcee.server.EducationServerEventHandler;
import au.com.grieve.geyser.reversion.editions.mcee.translators.v390_v407.Translator_mcee_v390_v407;
import au.com.grieve.geyser.reversion.editions.mcee.utils.TokenManager;
import au.com.grieve.geyser.reversion.server.ReversionServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServer;
import lombok.Getter;
import lombok.Value;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.plugin.GeyserPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides support for Minecraft Educational Edition
 */
@Getter
public class EducationEdition extends BaseEdition {
    @Getter
    private static EducationEdition instance;

    private final ReversionServerEventHandler serverEventHandler;

    private final List<TranslatorDefinition> translators = new ArrayList<>();

    private final TokenManager tokenManager;

    public EducationEdition(GeyserPlugin plugin) {
        super(plugin);

        instance = this;
        tokenManager = new TokenManager(plugin);
        serverEventHandler = new EducationServerEventHandler(this);
        getPlugin().registerEvents(this);

        // Register Translators
        registerTranslator(Translator_mcee_v390_v407.class, 390, 407);
    }

    @Override
    public BedrockServer hook(BedrockServer bedrockServer) {
        // Hook into Bedrock Server
        getPlugin().getLogger().debug("[Education]: Hooking into BedrockServer");
        return new ReversionBedrockServer(bedrockServer);
    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        GeyserConnector.getInstance().getBootstrap().getGeyserCommandManager().registerCommand(
                new EducationCommand("education", "Education Commands", "geyser.command.education", tokenManager));
    }

    public void registerTranslator(Class<? extends BaseTranslator> translatorClass, int protocolVersionFrom, int protocolVersionTo) {
        translators.add(new TranslatorDefinition(translatorClass, protocolVersionFrom, protocolVersionTo));
    }

    /**
     * Attempt to create a translator chain between the specific versions
     *
     * @param protocolVersionFrom version to translate from client
     * @param protocolVersionTo   version to translate to server
     * @param session             the Geyser session
     * @return start of Translator chain else null
     */
    public BaseTranslator createTranslator(int protocolVersionFrom, int protocolVersionTo, GeyserSession session) throws TranslatorException {
        // For now we assume we have a single translator that fits
        for (TranslatorDefinition definition : translators) {
            if (definition.getProtocolVersionFrom() == protocolVersionFrom && definition.getProtocolVersionTo() == protocolVersionTo) {
                try {
                    return definition.getTranslatorClass().getConstructor(GeyserSession.class).newInstance(session);
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new TranslatorException(e);
                }
            }
        }
        // No translator found so return the default
        try {
            return BaseTranslator.DefaultTranslator.class.getConstructor(GeyserSession.class).newInstance(session);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new TranslatorException(e);
        }
    }

//    private List<TranslatorDefinition> findShortestTranslatorChain(int protocolVersionFrom, int protocolVersionTo, List<TranslatorDefinition> definitions) {
//        List<TranslatorDefinition> best = new ArrayList<>();
//        for (TranslatorDefinition definition : definitions) {
//            if (definition.getProtocolVersionFrom() == protocolVersionFrom) {
//                if (definition.getProtocolVersionTo() == protocolVersionTo) {
//                    return Collections.singletonList(definition);
//                }
//
//                List<TranslatorDefinition> test = findShortestTranslatorChain(definition.getProtocolVersionTo(), protocolVersionTo, definitions.stream()
//                        .filter(d -> d != definition)
//                        .collect(Collectors.toList())
//                );
//
//                if (test == null || test.size() < best.size()-1) {
//                    continue;
//                }
//
//
//
//            }
//        }
//    }

    @Value
    public static class TranslatorDefinition {
        Class<? extends BaseTranslator> translatorClass;
        int protocolVersionFrom;
        int protocolVersionTo;

    }
}
