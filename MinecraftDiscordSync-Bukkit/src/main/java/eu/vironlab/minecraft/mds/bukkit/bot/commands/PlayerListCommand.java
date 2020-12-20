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

package eu.vironlab.minecraft.mds.bukkit.bot.commands;

import eu.vironlab.minecraft.mds.bukkit.BukkitMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.bukkit.server.BukkitServerUtil;
import eu.vironlab.minecraft.mds.discordbot.command.CommandData;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class PlayerListCommand extends DiscordCommand {

	public PlayerListCommand() {
		super("playerlist");
		setDescription("Shows the online players at the connected minecraft server");
	}

	@Override
	public boolean execute(CommandData data) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.playerlist.title", String.valueOf(BukkitServerUtil.getPlayerCount()), String.valueOf(BukkitServerUtil.getMaxPlayerCount())));
		embedBuilder.setColor(0x00ff00);
		String pl = BukkitServerUtil.getPlayerList();
		if(pl == "") pl = BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.playerlist.noplayers");
		if(pl.length() > 1980) pl = pl.substring(0, 1979) + "...";
		embedBuilder.setDescription("```markdown\n%pl%\n```".replace("%pl%", pl));
		embedBuilder.setFooter("Copyright © 2020 » vironlab.eu");
		data.reply(embedBuilder.build());
		return false;
	}

}
