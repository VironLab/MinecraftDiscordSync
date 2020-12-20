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

package eu.vironlab.minecraft.mds.nukkit.bot.listener;

import cn.nukkit.Server;
import eu.vironlab.minecraft.mds.nukkit.NukkitMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.nukkit.server.NukkitServerUtil;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		String status = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("bot.activity.message",
				"VironLab MinecraftDiscordSync");
		String statusType = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("bot.activity.type",
				"playing");
		String onlineType = NukkitMinecraftDiscordSync.getInstance().getConfig().getString("bot.activity.status",
				"online");
		OnlineStatus stat = null;
		Activity activity = null;
		switch (statusType.toLowerCase()) {
		case "idle":
			stat = OnlineStatus.IDLE;
			break;
		case "dnd":
			stat = OnlineStatus.DO_NOT_DISTURB;
			break;
		case "offline":
			stat = OnlineStatus.OFFLINE;
			break;
		default:
			stat = OnlineStatus.ONLINE;
			break;
		}
		switch (onlineType.toLowerCase()) {
		case "playing":
			activity = Activity.playing(status);
			break;
		case "listening":
			activity = Activity.listening(status);
			break;
		case "streaming":
			activity = Activity.streaming(status, "https://vironlab.eu/");
			break;
		default:
			activity = Activity.watching(status);
			break;
		}

		event.getJDA().getPresence().setPresence(stat, activity);

		if (NukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("onlinecounter.enabled", false)) {
			Server.getInstance().getScheduler().scheduleRepeatingTask(NukkitMinecraftDiscordSync.getInstance(), () -> {
				VoiceChannel channel = NukkitMinecraftDiscordSync.getInstance().getPluginAPI().getDiscordBot().getJDA()
						.getGuildById(NukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.id", ""))
						.getVoiceChannelById(NukkitMinecraftDiscordSync.getInstance().getConfig()
								.getString("onlinecounter.counterchannel", ""));
				if (channel != null) {
					String nameFormat = NukkitMinecraftDiscordSync.getInstance().getConfig()
							.getString("onlinecounter.channel_name", "(%online%/%maxonline%) Players Online");
					String channelName = nameFormat
							.replace("%online%", String.valueOf(NukkitServerUtil.getPlayerCount()))
							.replace("%maxonline%", String.valueOf(NukkitServerUtil.getMaxPlayerCount()));
					try {
						if (channel.getName() != channelName)
							channel.getManager().setName(channelName).queue();
					} catch (Exception e) {
					}
				}

			}, 20 * 60 * 3);

		}
		super.onReady(event);
	}

}