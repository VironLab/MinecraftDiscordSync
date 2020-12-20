/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.
 * 
 *      ___    _______                        ______         ______  
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ 
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ 
 *                                                             
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ 
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  
 * 
 *                                                         
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *   Contact:
 * 
 *     Discordserver:   https://discord.gg/wvcX92VyEH
 *     Website:         https://vironlab.eu/ 
 *     Mail:            contact@vironlab.eu
 *   
 */

package eu.vironlab.minecraft.mds.discordbot.command;

import java.util.HashMap;
import java.util.Map;

public class DiscordCommandMap {

	private Map<String, DiscordCommand> commands;

	public DiscordCommandMap() {
		commands = new HashMap<String, DiscordCommand>();
	}

	public boolean registerCommand(DiscordCommand command) {
		try {
			commands.put(command.getName(), command);
		} catch (Exception e) {
		}
		if (command.getAliases().size() > 0) {
			for (String alias : command.getAliases()) {
				try {
					commands.put(alias, command);
				} catch (Exception e) {
				}
			}
		}
		return true;
	}

	public DiscordCommand getCommand(String command) {
		return commands.get(command);
	}

	public boolean unregisterCommand(String command) {
		DiscordCommand dcCommand = getCommand(command);
		if (dcCommand.getAliases().size() > 0) {
			for (String alias : dcCommand.getAliases()) {
				try {
					commands.remove(alias);
				} catch (Exception e) {
				}
			}
		}
		try {
			commands.remove(command);
		} catch (Exception e) {
		}
		return true;
	}

	public Map<String, DiscordCommand> getCommands() {
		return commands;
	}

}
