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

package eu.vironlab.minecraft.mds.bungee;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

import eu.vironlab.minecraft.mds.MinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.PluginConstants;
import eu.vironlab.minecraft.mds.bungee.bot.DiscordSyncBot;
import eu.vironlab.minecraft.mds.bungee.commands.*;
import eu.vironlab.minecraft.mds.bungee.listener.*;
import eu.vironlab.minecraft.mds.bungee.logging.PluginLogger;
import eu.vironlab.minecraft.mds.bungee.permissions.LuckPermsPermissionHandler;
import eu.vironlab.minecraft.mds.bungee.storage.YAMLStorage;
import eu.vironlab.minecraft.mds.bungee.verification.BungeeDiscordVerifier;
import eu.vironlab.minecraft.mds.dependency.DependencyInjector;
import eu.vironlab.minecraft.mds.dependency.clazzloader.IPluginClassLoader;
import eu.vironlab.minecraft.mds.dependency.clazzloader.RefClassLoader;
import eu.vironlab.minecraft.mds.HeaderPrinter;
import eu.vironlab.minecraft.mds.IMinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.storage.MongoDBStorage;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeMinecraftDiscordSync extends Plugin {
	
	private static BungeeMinecraftDiscordSync instance;
	public static BungeeMinecraftDiscordSync getInstance() {
		return instance;
	}
	
	private IPluginClassLoader classLoader;
	private DependencyInjector dependencyInjector;
	
	private Configuration config;
	
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
		if(!this.getDataFolder().exists()) {
			this.getDataFolder().mkdirs();
		}
		
		this.saveDefaultConfig();
		this.saveResource("messages.ini", false);
		
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
		} catch (IOException e1) {
			e1.printStackTrace();
			this.getProxy().getPluginManager().unregisterListeners(this);
			this.getProxy().getPluginManager().unregisterCommands(this);
			this.onDisable();
		}
		
		if(this.getConfig().getInt("config-version", 0) != PluginConstants.CONFIG_VERSION) {
			try {
				FileUtils.moveFile(
					      FileUtils.getFile(this.getDataFolder() + "/config.yml"), 
					      FileUtils.getFile(this.getDataFolder() + "/config_old.yml"));
			} catch (Exception e) {
			}
			this.saveDefaultConfig();
		}
		
		File depPath = new File(this.getDataFolder(), "/libs/");
		if(!depPath.exists())
			depPath.mkdirs();
		
		classLoader = new RefClassLoader();
		dependencyInjector = new DependencyInjector(depPath, classLoader);
		
		dependencyInjector.loadJDA();
		
		dependencyInjector.require("org.slf4j:slf4j-simple:1.6.4");
		dependencyInjector.require("org.slf4j:slf4j-api:1.7.5");
		
		dependencyInjector.require("org.apache.commons:commons-lang3:3.11");
		dependencyInjector.require("commons-io:commons-io:2.8.0");
		
		dependencyInjector.require("org.mongodb:mongo-java-driver:3.12.7");
		
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
			default:
				permissionProvider = new LuckPermsPermissionHandler();
				break;
			}
		} catch (Exception e) {
			getLogger().warning(pluginMessages.translate("plugin.error.provider.permission"));
		}
		verificationProvider = new BungeeDiscordVerifier();
		
		MinecraftDiscordSyncAPI api = new MinecraftDiscordSyncAPI(discordBot, verificationProvider, permissionProvider, storageProvider, pluginLogger);
		api.setDependencyInjector(dependencyInjector);
		pluginAPI = api;
		
		if(!discordBot.startBot()) {
			this.getLogger().warning(pluginMessages.translate("plugin.bot.token_error"));
			this.getProxy().getPluginManager().unregisterListeners(this);
			this.getProxy().getPluginManager().unregisterCommands(this);
			this.onDisable();
			return;
		}
		
		this.registerEvents();
		this.registerCommands();
		
		this.getLogger().info(pluginMessages.translate("plugin.enabled"));
	}
	
	private void registerCommands() {
		PluginManager pluginManager = this.getProxy().getPluginManager();
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.discordinfo", false))
			pluginManager.registerCommand(this, new DiscordServerInfoCommand());
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.discord", false))
			pluginManager.registerCommand(this, new DiscordCommand());
		if(this.getConfig().getBoolean("plugin.commands_enabled.ingame.verify", false) && permissionProvider != null && storageProvider != null)
			pluginManager.registerCommand(this, new VerifyCommand());
	}
	
	private void registerEvents() {
		PluginManager pluginManager = this.getProxy().getPluginManager();
		pluginManager.registerListener(this, new ChatEventListener());
		if(this.getConfig().getBoolean("plugin.enable_playerevents", false))
			pluginManager.registerListener(this, new PlayerEventListener());
	}
	
	@Override
	public void onDisable() {
		this.discordBot.interrupt();
		this.getLogger().info(pluginMessages.translate("plugin.disabled"));
	}
	
	private void saveDefaultConfig() {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private void saveResource(String resourcePath, boolean overwrite) {
		File file = new File(getDataFolder(), resourcePath);
		if (!file.exists() || overwrite) {
            try (InputStream in = getResourceAsStream(resourcePath)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public Configuration getConfig() {
		return config;
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
		                getProxy().getLogger().info(ChatColor.translateAlternateColorCodes((char)'&', input.replace("%version%", "1.0.0-SNAPSHOT")));
		            bufferedReader.close();
		        } catch(Exception e) {
		        }
			}
		}.printHeader();
	}

}
