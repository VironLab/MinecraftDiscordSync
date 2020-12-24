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

import eu.vironlab.minecraft.mds.dependency.DependencyInjector;
import eu.vironlab.minecraft.mds.discordbot.IDiscordBot;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommandMap;
import eu.vironlab.minecraft.mds.logging.IPluginLogger;
import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;

public class MinecraftDiscordSyncAPI implements IMinecraftDiscordSyncAPI {
	
	private DependencyInjector dependencyInjector;
	
	private static MinecraftDiscordSyncAPI api;
	/**
	 * Returns the instance of the MinecraftDiscordSyncAPI
	 * @return instance
	 */
	public static MinecraftDiscordSyncAPI get() {
		return api;
	}
	/**
	 * Returns the instance of the MinecraftDiscordSyncAPI
	 * @return instance
	 */
	public static MinecraftDiscordSyncAPI getInstance() {
		return api;
	}
	
	private IVerificationProvider verificationProvider;
	private IPermissionProvider permissionProvider;
	private IStorageProvider storageProvider;
	
	private IDiscordBot discordBot;
	
	private IPluginLogger logger;
	

	public MinecraftDiscordSyncAPI(@NotNull IDiscordBot discordBot, @NotNull IVerificationProvider verificationProvider, 
			@NotNull IPermissionProvider permissionProvider, @NotNull IStorageProvider storageProvider, IPluginLogger logger) {
		this.discordBot = discordBot;
		this.verificationProvider = verificationProvider;
		this.permissionProvider = permissionProvider;
		this.storageProvider = storageProvider;
		this.logger = logger;
		api = this;
	}
	
	@Override
	public IPluginLogger getPluginLogger() {
		return logger;
	}

	@Override
	public IDiscordBot getDiscordBot() {
		return discordBot;
	}

	@Override
	public DiscordCommandMap getDiscordCommandMap() {
		return discordBot.getCommandMap();
	}

	@Override
	public boolean extendDiscordCommandMap(DiscordCommand... commands) {
		try {
			discordBot.addCommand(commands);
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public IPermissionProvider getPermissionProvider() {
		return permissionProvider;
	}

	@Override
	public boolean setPermissionProvider(@NotNull IPermissionProvider permissionProvider) {
		this.permissionProvider = permissionProvider;
		return true;
	}

	@Override
	public IStorageProvider getStorageProvider() {
		return storageProvider;
	}

	@Override
	public boolean setStorageProvider(@NotNull IStorageProvider storageProvider) {
		this.storageProvider = storageProvider;
		return true;
	}

	@Override
	public IVerificationProvider getVerificationProvider() {
		return verificationProvider;
	}

	@Override
	public boolean setVerificationProvider(@NotNull IVerificationProvider verificationProvider) {
		this.verificationProvider = verificationProvider;
		return true;
	}
	
	public void setDependencyInjector(DependencyInjector dependencyInjector) {
		this.dependencyInjector = dependencyInjector;
	}
	
	public DependencyInjector getDependencyInjector() {
		return dependencyInjector;
	}
	
}
