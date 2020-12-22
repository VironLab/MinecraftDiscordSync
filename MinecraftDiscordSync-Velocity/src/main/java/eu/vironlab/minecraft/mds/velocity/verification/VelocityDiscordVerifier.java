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

package eu.vironlab.minecraft.mds.velocity.verification;

import java.util.List;
import java.util.UUID;

import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.velocity.VelocityMinecraftDiscordSync;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;
import eu.vironlab.minecraft.mds.verification.VerifiedUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class VelocityDiscordVerifier implements IVerificationProvider {

	@Override
	public boolean verifyUser(UUID minecraftUUID, String discordID, boolean setVerified) {
		IStorageProvider storageProvider = VelocityMinecraftDiscordSync.getInstance().getPluginAPI().getStorageProvider();

		JDA bot = VelocityMinecraftDiscordSync.getInstance().getPluginAPI().getDiscordBot().getJDA();
		IPermissionProvider permissionProvider = VelocityMinecraftDiscordSync.getInstance().getPluginAPI()
				.getPermissionProvider();

		Guild guild = null;
		try {
			guild = bot.getGuildById(VelocityMinecraftDiscordSync.getInstance().getConfig().getString("guild.id", "123"));
		} catch (Exception e) {
			return false;
		}
		if (guild == null)
			return false;

		try {
			String primaryGroupName = permissionProvider.getPrimaryPlayerGroupName(minecraftUUID);

			try {
				setRoles(VelocityMinecraftDiscordSync.getInstance().getConfig()
						.getStringList("verification." + primaryGroupName + ".roles"), discordID, guild);
			} catch (Exception e) {
			}
			if (VelocityMinecraftDiscordSync.getInstance().getConfig()
					.exists("verification." + primaryGroupName + ".includes")) {
				for (String group : VelocityMinecraftDiscordSync.getInstance().getConfig()
						.getStringList("verification." + primaryGroupName + ".includes")) {
					try {
						setRoles(VelocityMinecraftDiscordSync.getInstance().getConfig()
								.getStringList("verification." + group + ".roles"), discordID, guild);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}

		return storageProvider.addVerifiedUser(discordID, minecraftUUID, setVerified);
	}

	private void setRoles(List<String> list, String discordID, Guild guild) {
		for (String groupId : list) {
			try {
				guild.addRoleToMember(guild.getMemberById(discordID), guild.getRoleById(groupId)).queue();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public boolean verifyUser(UUID minecraftUUID, String discordID) {
		return verifyUser(minecraftUUID, discordID, true);
	}

	@Override
	public boolean verifyUser(VerifiedUser user) {
		return verifyUser(user.getMinecraftUUID(), user.getDiscordID(), user.isVerified());
	}

	@Override
	public boolean isVerifiedUser(UUID minecraftUUID) {
		IStorageProvider storageProvider = VelocityMinecraftDiscordSync.getInstance().getPluginAPI().getStorageProvider();
		return storageProvider.getVerifiedUserObj(minecraftUUID).isVerified();
	}

	@Override
	public boolean isVerifiedUser(String discordID) {
		IStorageProvider storageProvider = VelocityMinecraftDiscordSync.getInstance().getPluginAPI().getStorageProvider();
		return storageProvider.getVerifiedUserObj(discordID).isVerified();
	}

}
