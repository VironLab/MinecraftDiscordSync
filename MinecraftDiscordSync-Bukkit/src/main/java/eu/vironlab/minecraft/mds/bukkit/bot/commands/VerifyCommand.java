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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import eu.vironlab.minecraft.mds.bukkit.BukkitMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.discordbot.command.CommandData;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.verification.VerificationQueue;

public class VerifyCommand extends DiscordCommand {

	public VerifyCommand() {
		super("verify");
		setDescription("Verification Process Command");
	}

	@Override
	public boolean execute(CommandData data) {
		
		if(data.getArgs().size() < 1) {
			data.reply(BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.verify.missingcode"));
			return false;
		}
		
		String code = data.getArgs().get(0);
		
		if(!VerificationQueue.verificationProcessing.containsValue(code)) {
			data.reply(BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.verify.wrongcode"));
			return false;
		}
		
		UUID playerUuid = getKeyByValue(VerificationQueue.verificationProcessing, code);
		VerificationQueue.verificationProcessing.remove(playerUuid);
		
		if(BukkitMinecraftDiscordSync.getInstance().getPluginAPI().getVerificationProvider().verifyUser(playerUuid, data.getAuthor().getId()))
			data.reply(BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.verify.success"));
		else 
			data.reply(BukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.discord.verify.error"));
		return false;
	}
	
	@SuppressWarnings("hiding")
	public static <UUID, String> UUID getKeyByValue(Map<UUID, String> map, String value) {
	    for (Entry<UUID, String> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

}
