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

package eu.vironlab.minecraft.mds.nukkit.commands;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import eu.vironlab.minecraft.mds.nukkit.NukkitMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.verification.VerificationQueue;

public class VerifyCommand extends PluginCommand<NukkitMinecraftDiscordSync> {

	public VerifyCommand(NukkitMinecraftDiscordSync owner) {
		super("verify", owner);
		setPermission("mcdcs.command.verify");
		setDescription("Verify yourselt at the discord server");
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if(!sender.hasPermission(this.getPermission())) return true;
		if(!(sender instanceof Player)) return true;
		String code = "";
		UUID uid = ((Player)sender).getUniqueId();
		if(!VerificationQueue.verificationProcessing.containsKey(uid)) {
			code = VerificationQueue.generateRandom6Digit();
			VerificationQueue.verificationProcessing.put(uid, code);
		}
		String botPrefix = NukkitMinecraftDiscordSync.getInstance().getPluginAPI().getDiscordBot().getPrefix();
		sender.sendMessage(NukkitMinecraftDiscordSync.getInstance().getPluginMessages().translate("command.ingame.verify.success", botPrefix, "verify", code));
		return false;
	}

}
