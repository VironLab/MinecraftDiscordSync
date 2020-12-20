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

package eu.vironlab.minecraft.mds.nukkit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;
import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.storage.MongoDBStorage;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;
import eu.vironlab.minecraft.mds.MinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.PluginConstants;
import eu.vironlab.minecraft.mds.HeaderPrinter;
import eu.vironlab.minecraft.mds.IMinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.nukkit.bot.DiscordSyncBot;
import eu.vironlab.minecraft.mds.nukkit.commands.*;
import eu.vironlab.minecraft.mds.nukkit.listener.*;
import eu.vironlab.minecraft.mds.nukkit.logging.PluginLogger;
import eu.vironlab.minecraft.mds.nukkit.permissions.LuckPermsPermissionHandler;
import eu.vironlab.minecraft.mds.nukkit.permissions.MultipassPermissionHandler;
import eu.vironlab.minecraft.mds.nukkit.storage.YAMLStorage;
import eu.vironlab.minecraft.mds.nukkit.verification.NukkitDiscordVerifier;

public class NukkitMinecraftDiscordSync extends PluginBase {
	
	private static NukkitMinecraftDiscordSync instance;
	public static NukkitMinecraftDiscordSync getInstance() {
		return instance;
	}
	
	private IMinecraftDiscordSyncAPI pluginAPI;
	
	private IStorageProvider storageProvider;
	private IPermissionProvider permissionProvider;
	private IVerificationProvider verificationProvider;
	
	private PluginLogger pluginLogger;
	
	private DiscordSyncBot discordBot;
	
	private Messages pluginMessages;
	
	@Override
	public void onLoad() {
		this.logHeader();
		instance = this;
	}
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.saveResource("messages.ini");
		
		if(this.getConfig().getInt("config-version", 0) != PluginConstants.CONFIG_VERSION) {
			try {
				FileUtils.moveFile(
					      FileUtils.getFile(this.getDataFolder() + "/config.yml"), 
					      FileUtils.getFile(this.getDataFolder() + "/config_old.yml"));
			} catch (Exception e) {
			}
			this.saveDefaultConfig();
		}
		
		pluginMessages = new Messages();
		pluginMessages.load(this.getDataFolder());
		
		pluginLogger =  new PluginLogger();
		
		discordBot = new DiscordSyncBot(pluginLogger, this.getConfig().getString("bot.token", "NaN"), this.getConfig().getString("bot.prefix"));
		switch (this.getConfig().getString("plugin.prividers.storage_provider", "yaml").toLowerCase()) {
		case "mongodb":
			storageProvider = new MongoDBStorage();
			try {
				storageProvider.connect(
						this.getConfig().getString("plugin.database.username"), 
						this.getConfig().getString("plugin.database.host"),
						this.getConfig().getString("plugin.database.password"), 
						this.getConfig().getInt("plugin.database.port"), 
						this.getConfig().getString("plugin.database.database")
				);
			} catch (Exception e) {
				storageProvider = null;
				getLogger().warning(pluginMessages.translate("plugin.error.provider.storage"));
			}
			break;
		default:
			storageProvider = new YAMLStorage(this);
			break;
		}
		
		try {
			switch (this.getConfig().getString("plugin.prividers.permission_provider", "luckperms").toLowerCase()) {
			case "multipass":
				permissionProvider = new MultipassPermissionHandler();
				break;
			default:
				permissionProvider = new LuckPermsPermissionHandler();
				break;
			}
			
		} catch (Exception e) {
			getLogger().warning(pluginMessages.translate("plugin.error.provider.permission"));
		}
		verificationProvider = new NukkitDiscordVerifier();
		pluginAPI = new MinecraftDiscordSyncAPI(discordBot, verificationProvider, permissionProvider, storageProvider, pluginLogger);
		
		if(!discordBot.startBot()) {
			this.getLogger().warning(pluginMessages.translate("plugin.bot.token_error"));
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		this.registerEvents();
		this.registerCommands();
			
		this.getLogger().info(pluginMessages.translate("plugin.enabled"));
	}
	
	private void registerCommands() {
		CommandMap commandMap = this.getServer().getCommandMap();
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.discordinfo", false))
			commandMap.register("minecraftdiscordsync", new DiscordServerInfoCommand(this));
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.discord", false))
			commandMap.register("minecraftdiscordsync", new DiscordCommand(this));
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.verify", false) && permissionProvider != null && storageProvider != null)
			commandMap.register("minecraftdiscordsync", new VerifyCommand(this));
	}
	
	private void registerEvents() {
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new ChatEventListener(), this);
		if(this.getConfig().getBoolean("plugin.enable_playerevents", false))
			pluginManager.registerEvents(new PlayerEventListener(), this);
	}
	
	@Override
	public void onDisable() {
		this.discordBot.interrupt();
		this.getLogger().info(pluginMessages.translate("plugin.disabled"));
	}
	
	public IMinecraftDiscordSyncAPI getPluginAPI() {
		return pluginAPI;
	}
	
	public Messages getPluginMessages() {
		return pluginMessages;
	}
	
	private void logHeader() {
		new HeaderPrinter() {
			@Override
			public void printHeader() {
				try {
		            InputStream inputStream = HeaderPrinter.class.getClassLoader().getResourceAsStream("header.txt");
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		            String input;
		            while ((input = bufferedReader.readLine()) != null)
		                getServer().getLogger().info(TextFormat.colorize((char)'&', input.replace("%version%", "1.0.0-SNAPSHOT")));
		            bufferedReader.close();
		        } catch(Exception e) {
		        }
			}
		}.printHeader();
	}

}
