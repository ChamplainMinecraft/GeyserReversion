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

package au.com.grieve.geyser.reversion.editions.mcee.commands;

import au.com.grieve.geyser.reversion.editions.mcee.utils.TokenManager;
import org.geysermc.connector.command.CommandSender;
import org.geysermc.connector.command.GeyserCommand;
import org.geysermc.connector.common.ChatColor;

public class EducationCommand extends GeyserCommand {

    private final TokenManager tokenManager;

    public EducationCommand(String name, String description, String permission, TokenManager tokenManager) {
        super(name, description, permission);
        this.tokenManager = tokenManager;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("---- Education SubCommands ----");
        sender.sendMessage(ChatColor.YELLOW + "/education new" + ChatColor.WHITE + "    - Create new Authorization URL");
        sender.sendMessage(ChatColor.YELLOW + "/education confirm" + ChatColor.WHITE + "    - Confirm an Authorization Response");
        sender.sendMessage("");
        sender.sendMessage("Use '" + ChatColor.YELLOW + "new" + ChatColor.WHITE + "' to generate a URL that you copy into your browser.");
        sender.sendMessage("This will allow you to log into your MCEE account. Once done you will have a white page with a URL both in");
        sender.sendMessage("its title as well as address bar. Copy the full address and provide it as a parameter to '" + ChatColor.YELLOW + "confirm" + ChatColor.WHITE + "'.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.isConsole()) {
            return;
        }

        if (args.length == 0) {
            showHelp(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "new":
                sender.sendMessage("Copy and paste the following into your web browser:");
                sender.sendMessage("");
                sender.sendMessage("   " + ChatColor.YELLOW + tokenManager.getNewAuthorizationUrl().toString());
                break;
            case "confirm":
                if (args.length < 2) {
                    sender.sendMessage("Missing parameter");
                    return;
                }
                try {
                    tokenManager.createInitialToken(args[1]);
                } catch (TokenManager.TokenException e) {
                    sender.sendMessage("Error: " + e.getMessage());
                }
                sender.sendMessage("Successfully created new token");
                break;
            default:
                showHelp(sender);
        }
    }
}
