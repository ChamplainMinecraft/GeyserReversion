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
import au.com.grieve.geyser.reversion.editions.mcee.commands.EducationCommand;
import au.com.grieve.geyser.reversion.editions.mcee.hook.ReversionBedrockServer;
import au.com.grieve.geyser.reversion.editions.mcee.translators.v390_v407.Translator;
import au.com.grieve.geyser.reversion.editions.mcee.utils.TokenManager;
import com.nukkitx.protocol.bedrock.BedrockServer;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.plugin.GeyserPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides support for Minecraft Educational Edition
 */
@Getter
public class EducationEdition extends BaseEdition {
    @Getter
    private static EducationEdition instance;

    private final Map<Integer, Class<? extends BaseTranslator>> translators = new HashMap<>();
    private final List<BaseTranslator> activeTranslators = new ArrayList<>();

    private final TokenManager tokenManager;

    public EducationEdition(GeyserPlugin plugin) {
        super(plugin);

        instance = this;
        tokenManager = new TokenManager(plugin);
        getPlugin().registerEvents(this);

        // Register Translators
        registerTranslator(390, Translator.class);
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

    public void registerTranslator(int protocolVersion, Class<? extends BaseTranslator> cls) {
        translators.put(protocolVersion, cls);
    }
}
