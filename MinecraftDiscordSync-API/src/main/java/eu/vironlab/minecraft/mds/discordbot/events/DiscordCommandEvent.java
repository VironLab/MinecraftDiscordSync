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

package eu.vironlab.minecraft.mds.discordbot.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eu.vironlab.minecraft.mds.discordbot.IDiscordBot;
import eu.vironlab.minecraft.mds.discordbot.command.CommandData;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordCommandEvent extends ListenerAdapter {

	private String prefix;
	
	private IDiscordBot discordBot;

	public DiscordCommandEvent(String prefix, IDiscordBot discordBot) {
		this.prefix = prefix;
		this.discordBot = discordBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		// do nothing when author bot
		if (event.getMessage().getAuthor().isBot()) return;
		// do nothing if empty message
		if (event.getMessage().getContentRaw() == null || event.getMessage().getContentRaw() == "") return;
		// do nothing if message don't start with prefix
		if (event.getMessage().getContentRaw().indexOf(prefix) != 0) return;
		
		// split all args to List
		List<String> args = new ArrayList<String>(Arrays.asList(StringUtils.split(event.getMessage().getContentRaw().trim())));
		
		String command = args.remove(0);
		if (!command.startsWith(prefix)) return;
		
		try {
			// get botcommand from commandMap
			DiscordCommand cmd = discordBot.getCommandMap().getCommands().get(command.substring(prefix.length()));
			if (cmd == null) return;
			// run command async
			new Thread(() -> {
				try {
					cmd.execute(new CommandData(event, discordBot, args));
				} catch (Exception e) {
					discordBot.getPluginLogger().error("Exception in Command: \"" + command + "\"\n" + e);
				}
			}).start();
		} catch (Exception e) {
			discordBot.getPluginLogger().error(e);
		}

		super.onMessageReceived(event);
	}

	public String getPrefix() {
		return prefix;
	}

}
