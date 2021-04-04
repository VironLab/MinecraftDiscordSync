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

package eu.vironlab.minecraft.mds.sponge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import eu.vironlab.minecraft.mds.MinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.PluginConstants;
import eu.vironlab.minecraft.mds.dependency.DependencyInjector;
import eu.vironlab.minecraft.mds.dependency.clazzloader.IPluginClassLoader;
import eu.vironlab.minecraft.mds.dependency.clazzloader.RefClassLoader;
import eu.vironlab.minecraft.mds.HeaderPrinter;
import eu.vironlab.minecraft.mds.IMinecraftDiscordSyncAPI;
import eu.vironlab.minecraft.mds.permissions.IPermissionProvider;
import eu.vironlab.minecraft.mds.sponge.bot.DiscordSyncBot;
import eu.vironlab.minecraft.mds.sponge.commands.*;
import eu.vironlab.minecraft.mds.sponge.configuration.Config;
import eu.vironlab.minecraft.mds.sponge.listener.*;
import eu.vironlab.minecraft.mds.sponge.logging.PluginLogger;
import eu.vironlab.minecraft.mds.sponge.permissions.LuckPermsPermissionHandler;
import eu.vironlab.minecraft.mds.sponge.storage.YAMLStorage;
import eu.vironlab.minecraft.mds.sponge.verification.SpongeDiscordVerifier;
import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.storage.MongoDBStorage;
import eu.vironlab.minecraft.mds.verification.IVerificationProvider;

@Plugin(id = "smcdcs", name = "SpongeMinecraftDiscordSyncPlugin", version = "1.0.1-SNAPSHOT", authors = {
		"VironLab" }, url = "https://vironlab.eu")
public class SpongeMinecraftDiscordSync {
	
	public static boolean enabled = true;
	
	private Server server;

	@Inject
	private Logger logger;

	private static SpongeMinecraftDiscordSync instance;

	public static SpongeMinecraftDiscordSync getInstance() {
		return instance;
	}
	
	private IPluginClassLoader classLoader;
	private DependencyInjector dependencyInjector;

	private IMinecraftDiscordSyncAPI pluginAPI;

	private IStorageProvider storageProvider;
	private IPermissionProvider permissionProvider;
	private IVerificationProvider verificationProvider;

	private PluginLogger pluginLogger;
	private DiscordSyncBot discordBot;

	private Messages pluginMessages;

	private File dataFolder;

	private Config config;
	
	@Listener
    public void onServerStart(GameStartedServerEvent event) {
		onLoad();
		onEnable();
    }

	public void onLoad() {
		this.logHeader();
		instance = this;
	}

	public void onEnable() {
		dataFolder = new File("mods/smcdcs");
		if(!dataFolder.exists())
			dataFolder.mkdirs();
		
		this.saveDefaultConfig();
		this.saveResource("messages.ini", false);

		this.config = new Config(new File(getDataFolder(), "config.yml"));

		if (this.getConfig().getInt("config-version", 0) != PluginConstants.CONFIG_VERSION) {
			try {
				FileUtils.moveFile(FileUtils.getFile(this.getDataFolder() + "/config.yml"),
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

		pluginLogger = new PluginLogger();

		discordBot = new DiscordSyncBot(pluginLogger, this.getConfig().getString("bot.token", "NaN"),
				this.getConfig().getString("bot.prefix"));
		switch (this.getConfig().getString("plugin.providers.storage_provider", "yaml").toLowerCase()) {
		case "mongodb":
			storageProvider = new MongoDBStorage();
			try {
				storageProvider.connect(this.getConfig().getString("plugin.database.username"),
						this.getConfig().getString("plugin.database.host"),
						this.getConfig().getString("plugin.database.password"),
						this.getConfig().getInt("plugin.database.port"),
						this.getConfig().getString("plugin.database.database"));
			} catch (Exception e) {
				storageProvider = null;
				getLogger().warn(pluginMessages.translate("plugin.error.provider.storage"));
			}
			break;
		default:
			storageProvider = new YAMLStorage(this);
			break;
		}

		try {
			switch (this.getConfig().getString("plugin.providers.permission_provider", "luckperms").toLowerCase()) {
			default:
				permissionProvider = new LuckPermsPermissionHandler();
				break;
			}
		} catch (Exception e) {
			getLogger().warn(pluginMessages.translate("plugin.error.provider.permission"));
		}
		verificationProvider = new SpongeDiscordVerifier();
		
		MinecraftDiscordSyncAPI api = new MinecraftDiscordSyncAPI(discordBot, verificationProvider, permissionProvider, storageProvider, pluginLogger);
		api.setDependencyInjector(dependencyInjector);
		pluginAPI = api;

		if (!discordBot.startBot()) {
			this.getLogger().warn(pluginMessages.translate("plugin.bot.token_error"));
			disablePlugin();
			return;
		}

		this.registerEvents();
		this.registerCommands();

		this.getLogger().info(pluginMessages.translate("plugin.enabled"));
	}

	private void registerCommands() {

		CommandSpec meta1 = CommandSpec.builder()
			    .description(Text.of("Hello World Command"))
			    .permission("mcdcs.command.discordinfo")
			    .executor(new DiscordServerInfoCommand())
			    .build();
		
		CommandSpec meta2 = CommandSpec.builder()
			    .description(Text.of("Hello World Command"))
			    .permission("mcdcs.command.discord")
			    .executor(new DiscordCommand())
			    .build();
		
		CommandSpec meta3 = CommandSpec.builder()
			    .description(Text.of("Hello World Command"))
			    .permission("mcdcs.command.verify")
			    .executor(new VerifyCommand())
			    .build();

		if (this.getConfig().getBoolean("plugin.commands_enabled.ingame.discordinfo", false)) {
			Sponge.getCommandManager().register(this, meta1, "discordinfo", "dcinfo");
		}
		if (this.getConfig().getBoolean("plugin.commands_enabled.ingame.discord", false))
			Sponge.getCommandManager().register(this, meta2, "discord");
		if (this.getConfig().getBoolean("plugin.commands_enabled.ingame.verify", false) && permissionProvider != null
				&& storageProvider != null)
			Sponge.getCommandManager().register(this, meta3, "verify");
	}

	private void registerEvents() {
		Sponge.getEventManager().registerListeners(this, new ChatEventListener());
		if (this.getConfig().getBoolean("plugin.enable_playerevents", false))
			Sponge.getEventManager().registerListeners(this, new PlayerEventListener());
		
	}

	private void disablePlugin() {
	    this.discordBot.interrupt();
	    enabled = false;
	    MinecraftDiscordSyncAPI.get().setVerificationProvider(null);
	    MinecraftDiscordSyncAPI.get().setPermissionProvider(null);
	    MinecraftDiscordSyncAPI.get().setStorageProvider(null);
	    MinecraftDiscordSyncAPI.get().setDependencyInjector(null);
		this.getLogger().info(pluginMessages.translate("plugin.disabled"));
	}

	private void saveDefaultConfig() {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveResource(String resourcePath, boolean overwrite) {
		if(!resourcePath.startsWith("/")) resourcePath = "/" + resourcePath;
		File file = new File(getDataFolder(), resourcePath);
		if (!file.exists() || overwrite) {
			try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
						getLogger().info(ChatColor.translateAlternateColorCodes('&', input.replace("%version%", this.getVersion())));
					bufferedReader.close();
				} catch (Exception e) {
				}
			}
		}.printHeader();
	}

	public Logger getLogger() {
		return logger;
	}

	public Server getServer() {
		return server;
	}

	public File getDataFolder() {
		return dataFolder;
	}

	public Config getConfig() {
		return config;
	}
	
}
