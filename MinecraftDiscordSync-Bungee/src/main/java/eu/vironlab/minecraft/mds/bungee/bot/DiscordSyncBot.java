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

package eu.vironlab.minecraft.mds.bungee.bot;

import java.util.ArrayList;
import java.util.List;

import eu.vironlab.minecraft.mds.bungee.BungeeMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.bungee.bot.commands.*;
import eu.vironlab.minecraft.mds.bungee.bot.listener.*;
import eu.vironlab.minecraft.mds.discordbot.DiscordBot;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.logging.IPluginLogger;

public class DiscordSyncBot extends DiscordBot {

	public DiscordSyncBot(IPluginLogger pluginLogger, String token, String commandPrefix) {
		super(pluginLogger, token, commandPrefix);
		
		List<DiscordCommand> defaultCommands = new ArrayList<DiscordCommand>();
		
		defaultCommands.add(new AboutCommand());
		
		if(BungeeMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.commands_enabled.discord.online", false))
			defaultCommands.add(new OnlineCommand());
		if(BungeeMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.commands_enabled.discord.playerlist", false))
			defaultCommands.add(new PlayerListCommand());
		if(BungeeMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.commands_enabled.discord.serverinfo", false))
			defaultCommands.add(new ServerInfoCommand());	
		if(BungeeMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.commands_enabled.discord.verify", false))
			defaultCommands.add(new VerifyCommand());
		
		defaultCommands.forEach(command -> {
			addCommand(command);
		});
		
		addListenerBeforeStart(new ReadyListener());
		addListenerBeforeStart(new GuildMemberListener());
		addListenerBeforeStart(new ChatListener());
		
	}

}
