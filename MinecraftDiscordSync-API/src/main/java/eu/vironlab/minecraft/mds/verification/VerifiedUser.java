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

package eu.vironlab.minecraft.mds.verification;

import java.util.UUID;

import eu.vironlab.minecraft.mds.storage.IStorageProvider;

public class VerifiedUser {
	
	private UUID minecraftUUID;
	private String discordID;
	
	private IStorageProvider storageProvider;
	
	private boolean verified = false;
	
	public VerifiedUser(UUID minecraftUUID, String discordID, IStorageProvider storageProvider) {
		this.minecraftUUID = minecraftUUID;
		this.discordID = discordID;
		this.storageProvider = storageProvider;
	}
	
	public VerifiedUser(UUID minecraftUUID, String discordID,  boolean verified, IStorageProvider storageProvider) {
		this.minecraftUUID = minecraftUUID;
		this.discordID = discordID;
		this.verified = verified;
		this.storageProvider = storageProvider;
	}
	
	public UUID getMinecraftUUID() {
		return minecraftUUID;
	}
	
	public String getDiscordID() {
		return discordID;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	public VerifiedUser setVerified(IVerificationProvider provider, boolean verified) {
		provider.verifyUser(minecraftUUID, discordID);
		this.verified = verified;
		return this;
	}
	
	public VerifiedUser setVerified(boolean verified) {
		this.verified = verified;
		return this;
	}
	
	public IStorageProvider getStorageProvider() {
		return storageProvider;
	}

}
