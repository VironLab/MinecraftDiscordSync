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

package eu.vironlab.minecraft.mds.bungee.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.vironlab.minecraft.mds.configuration.AbstractPluginConfigurationProvider;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigProviderImpl extends AbstractPluginConfigurationProvider {
	
	private Configuration config;

	public ConfigProviderImpl(File configurationFile) {
		super(configurationFile);
		if(!getConfigurationFile().exists()) {
			getConfigurationFile().mkdir();
		}
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigurationFile());
		} catch (IOException e) {
		}
	}

	@Override
	public String getString(String key) {
		return config.getString(key);
	}

	@Override
	public int getInteger(String key) {
		return config.getInt(key, 0);
	}

	@Override
	public long getLong(String key) {
		return config.getLong(key);
	}

	@Override
	public float getFloat(String key) {
		return 0;
	}

	@Override
	public boolean getBoolean(String key) {
		return config.getBoolean(key, false);
	}

	@Override
	public List<String> getStringList(String key) {
		return config.getStringList(key);
	}

	@Override
	public List<Integer> getIntegerList(String key) {
		return config.getIntList(key);
	}

	@Override
	public boolean load() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigurationFile());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, getConfigurationFile());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean reload() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigurationFile());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
