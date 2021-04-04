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

package eu.vironlab.minecraft.mds.bukkit.bot.listener;

import eu.vironlab.minecraft.mds.bukkit.BukkitMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.bukkit.server.BukkitServerUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(!BukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("chatsync.discord_to_ingame", false)) return;
		if(BukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.chatchannel_id", "123") != event.getChannel().getId()) return;
		
		String message = event.getMessage().getContentRaw();
		if(message == null || message == "") return;
		
		String messageFormat = BukkitMinecraftDiscordSync.getInstance().getConfig().getString("format.ingamechat", "[%pluginprefix%] » %playername% » %message%");
		String pluginPrefix = BukkitMinecraftDiscordSync.getInstance().getConfig().getString("plugin.prefix", "MinecraftDiscordSync");
		String messageToSend = messageFormat.replace("%pluginprefix%", pluginPrefix)
				.replace("%playername%", event.getAuthor().getName())
				.replace("%message%", message);
		
		BukkitServerUtil.broadcastMessage(messageToSend);
		
		super.onGuildMessageReceived(event);
	}

}
