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
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberListener extends ListenerAdapter {
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(event == null) return;
		if(!BukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.enable_discord_memberevents", false)) return;
		
		String guildId =  BukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.id", "123456789");
		if(event.getGuild().getId() != guildId) return;
		
		String username = event.getMember().getUser().getName();
		String discrim = event.getMember().getUser().getDiscriminator();
		String servername = event.getGuild().getName();
		String messageFormat = BukkitMinecraftDiscordSync.getInstance().getConfig().getString("format.memberjoin_discordserver", "&a%username%#%discriminator% joined the Discordserver &e%servername%");
		String message = messageFormat.replace("%username%", username).replace("%discriminator%", discrim).replace("%servername%", servername);
		
		BukkitServerUtil.broadcastMessage(message);
		
		super.onGuildMemberJoin(event);
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		if(event == null) return;
		if(!BukkitMinecraftDiscordSync.getInstance().getConfig().getBoolean("plugin.enable_discord_memberevents", false)) return;
		
		String guildId =  BukkitMinecraftDiscordSync.getInstance().getConfig().getString("guild.id", "123456789");
		if(event.getGuild().getId() != guildId) return;
		
		String username = event.getMember().getUser().getName();
		String discrim = event.getMember().getUser().getDiscriminator();
		String servername = event.getGuild().getName();
		String messageFormat = BukkitMinecraftDiscordSync.getInstance().getConfig().getString("format.memberremove_discordserver", "&c%username%#%discriminator% left the Discordserver &e%servername%");
		String message = messageFormat.replace("%username%", username).replace("%discriminator%", discrim).replace("%servername%", servername);
		
		BukkitServerUtil.broadcastMessage(message);
		
		super.onGuildMemberRemove(event);
	}

}
