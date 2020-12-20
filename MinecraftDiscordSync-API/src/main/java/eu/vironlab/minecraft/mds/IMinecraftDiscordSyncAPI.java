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

package eu.vironlab.minecraft.mds;

import org.jetbrains.annotations.NotNull;

import eu.vironlab.minecraft.mds.discordbot.IDiscordBot;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommandMap;
import eu.vironlab.minecraft.mds.logging.IPluginLogger;
import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;

public interface IMinecraftDiscordSyncAPI {
	
	/**
	 * Get the plugin Logger instancea
	 * @return pluginLogger
	 */
	public IPluginLogger getPluginLogger();
	
	/**
	 * get the DiscordBot instance
	 * @return discordBot
	 */
	public IDiscordBot getDiscordBot();
	
	/**
	 * get the DiscordCommandMap
	 * @return commandMap
	 */
	public DiscordCommandMap getDiscordCommandMap();
	/**
	 * Extend the DiscordBot Command Map with more DiscordCommands
	 * @param commands
	 * @return success
	 */
	public boolean extendDiscordCommandMap(DiscordCommand ... commands);
	
	/**
	 * get the permission provider
	 * @return permissionProvider
	 */
	public IPermissionProvider getPermissionProvider();
	/**
	 * set a own permission provider in the case youre using your own plugins.
	 * @param permissionProvider
	 * @return
	 */
	public boolean setPermissionProvider(@NotNull IPermissionProvider permissionProvider);
	
	/**
	 * Get the storage provider wich manages the verified users storage
	 * @return storageProvider
	 */
	public IStorageProvider getStorageProvider();
	/**
	 * set your own storage provider in case you dont want to use the default providers
	 * @param storageProvider
	 * @return success
	 */
	public boolean setStorageProvider(@NotNull IStorageProvider storageProvider);
	
	/**
	 * Get the verification provider wich manages verification
	 * @return varificationProvider
	 */
	public IVerificationProvider getVerificationProvider();
	/**
	 * set a new verification provider
	 * @param verificationProvider
	 * @return success
	 */
	public boolean setVerificationProvider(@NotNull IVerificationProvider verificationProvider);
	
	
}
