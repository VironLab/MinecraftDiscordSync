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

package eu.vironlab.minecraft.mds.bungee.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import eu.vironlab.minecraft.mds.storage.IStorageProvider;
import eu.vironlab.minecraft.mds.verification.VerifiedUser;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YAMLStorage implements IStorageProvider {

	private final String collectionRoot = "verified_users";
	private final String verifiedKey = "Verified";
	private final String discordIDKey = "DiscordID";

	private Configuration config;
	private File configFile;

	public YAMLStorage(Plugin plugin) {
		configFile = new File(plugin.getDataFolder(), "storage.yml");
		if (!configFile.exists()) {
			try {
				Files.copy(IOUtils.toInputStream("#internal storage", "UTF-8"), configFile.toPath());
			} catch (Exception e) {
			}
        }
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (IOException e) {
		}
		if (!config.contains(collectionRoot)) {
			config.set(collectionRoot + ".storage", true);
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void connect(String user, String host, String password, int port, String dbName) {
	}

	@Override
	public void disconnect() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
		} catch (IOException e) {
		}
	}

	@Override
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID, boolean verified) {
		config.set(collectionRoot + "." + minecraftUUID.toString() + "." + discordIDKey, discordID);
		config.set(collectionRoot + "." + minecraftUUID.toString() + "." + verifiedKey, verified);
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID) {
		return addVerifiedUser(discordID, minecraftUUID, true);
	}

	@Override
	public boolean removeVerifiedUser(String discordID) {
		for (String key : config.getSection(collectionRoot).getKeys()) {
			if (config.getString(collectionRoot + "." + key + "." + discordIDKey) == discordID) {
				return removeVerifiedUser(UUID.fromString(collectionRoot + "." + key));
			}
		}
		return false;
	}

	@Override
	public boolean removeVerifiedUser(UUID minecraftUUID) {
		config.set(collectionRoot + "." + minecraftUUID.toString(), null);
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public VerifiedUser getVerifiedUserObj(String discordID) {
		for (String key : config.getSection(collectionRoot).getKeys()) {
			if (config.getString(collectionRoot + "." + key + "." + discordIDKey) == discordID) {
				return getVerifiedUserObj(UUID.fromString(key));
			}
		}
		return null;
	}

	@Override
	public VerifiedUser getVerifiedUserObj(UUID minecraftUUID) {
		return new VerifiedUser(minecraftUUID,
				config.getString(collectionRoot + "." + minecraftUUID.toString() + "." + discordIDKey),
				config.getBoolean(collectionRoot + "." + minecraftUUID.toString() + "." + verifiedKey), this);
	}

}
