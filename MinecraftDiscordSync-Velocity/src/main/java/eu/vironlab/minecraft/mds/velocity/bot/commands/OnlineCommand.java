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

package eu.vironlab.minecraft.mds.velocity.bot.commands;

import eu.vironlab.minecraft.mds.discordbot.command.CommandData;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.velocity.VelocityMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.velocity.server.VelocityServerUtil;
import net.dv8tion.jda.api.EmbedBuilder;

public class OnlineCommand extends DiscordCommand {

	public OnlineCommand() {
		super("online");
		setDescription("Counts the online players at the connected minecraft server");
	}

	@Override
	public boolean execute(CommandData data) {
		if(!VelocityMinecraftDiscordSync.enabled) return true;
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(VelocityMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.online.title", String.valueOf(VelocityServerUtil.getPlayerCount()), String.valueOf(VelocityServerUtil.getMaxPlayerCount())));
		embedBuilder.setColor(0x00ff00);
		embedBuilder.setFooter("Copyright © 2020 » vironlab.eu");
		data.reply(embedBuilder.build());
		return false;
	}

}
