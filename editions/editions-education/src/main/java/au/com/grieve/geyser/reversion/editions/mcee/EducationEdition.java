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

import au.com.grieve.geyser.reversion.ReversionManager;
import au.com.grieve.geyser.reversion.api.BaseEdition;
import au.com.grieve.geyser.reversion.editions.mcee.commands.EducationCommand;
import au.com.grieve.geyser.reversion.editions.mcee.server.EducationServerEventHandler;
import au.com.grieve.geyser.reversion.editions.mcee.translators.ee390_be408.Translator_ee390_be408;
import au.com.grieve.geyser.reversion.editions.mcee.utils.TokenManager;
import au.com.grieve.geyser.reversion.server.ReversionServerEventHandler;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;


/**
 * Provides support for Minecraft Educational Edition
 */
@Getter
public class EducationEdition extends BaseEdition {
    @Getter
    private static EducationEdition instance;

    private final ReversionServerEventHandler serverEventHandler;

    private final ReversionManager manager;
    private final TokenManager tokenManager;

    public EducationEdition(ReversionManager manager) {
        instance = this;

        this.manager = manager;
        tokenManager = new TokenManager(manager.getPlugin());
        serverEventHandler = new EducationServerEventHandler();
        manager.getPlugin().registerEvents(this);

        // Register Translators
        registerTranslators();
    }

    public void registerTranslators() {
        manager.registerTranslator("education", 390, "bedrock", 408, Translator_ee390_be408.class);

    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        GeyserConnector.getInstance().getBootstrap().getGeyserCommandManager().registerCommand(
                new EducationCommand("education", "Education Commands", "geyser.command.education", tokenManager));
    }

}
