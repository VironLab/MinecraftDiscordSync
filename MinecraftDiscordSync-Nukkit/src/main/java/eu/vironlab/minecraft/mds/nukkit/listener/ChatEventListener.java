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

package eu.vironlab.minecraft.mds.nukkit.listener;

import java.awt.Color;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import eu.vironlab.minecraft.mds.nukkit.NukkitMinecraftDiscordSync;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChatEventListener implements Listener {
	
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if(event == null) return;
		if(event.isCancelled()) return;
		if(!NukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("chatsync.ingame_to_discord", false)) return;
		
		TextChannel chatChannel = null;
		try {
			chatChannel = getChannel();
		} catch(Exception e) {}
		if(chatChannel == null) return;
		
		boolean useEmbed = NukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("format.use_embeds", false);
		String messageFormat = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("format.discordchat", "[%pluginprefix%] » %playername% » %message%");
		String pluginPrefix = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("plugin.prefix", "MinecraftDiscordSync");
		String message = messageFormat.replace("%pluginprefix%", pluginPrefix)
				.replace("%playername%", TextFormat.clean(event.getPlayer().getDisplayName()))
				.replace("%message%", TextFormat.clean(event.getMessage()));
		
		if(!useEmbed) {
			chatChannel.sendMessage(message).queue();
		} else {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setColor(NukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("format.embedcolor_randomized", false) ? getRandomColor() : getColor());
			embedBuilder.setDescription(message);
			embedBuilder.setAuthor(event.getPlayer().getName());
			embedBuilder.setFooter(event.getPlayer().getUniqueId().toString());
			chatChannel.sendMessage(embedBuilder.build()).queue();;
		}
		
	}

	private TextChannel getChannel() {
		return NukkitMinecraftDiscordSync.getInstance().getPluginAPI().getDiscordBot().getJDA()
				.getGuildById(NukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.id"))
				.getTextChannelById(NukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.chatchannel_id"));
	}
	
	private Color getRandomColor() {
		return new Color((int) (Math.random() * 1.6777216E7));
	}
	
	private Color getColor() {
		String hexString = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("embed.colors.messages", "#ffe020");
		if(!hexString.startsWith("#")) hexString = "#" + hexString;
		return hex2Rgb(hexString);
	}
	
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}

}
