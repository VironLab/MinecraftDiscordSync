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
 *     DiscordBukkit:   https://discord.gg/wvcX92VyEH
 *     Website:         https://vironlab.eu/ 
 *     Mail:            contact@vironlab.eu
 *   
 */

package eu.vironlab.minecraft.mds.sponge.logging;

import eu.vironlab.minecraft.mds.logging.IPluginLogger;
import eu.vironlab.minecraft.mds.sponge.SpongeMinecraftDiscordSync;

public class PluginLogger implements IPluginLogger {

	@Override
	public void log(String msg) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().info(msg);
	}

	@Override
	public void error(String msg) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().info(msg);
	}

	@Override
	public void warn(String msg) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().warn(msg);
	}

	@Override
	public void debug(String msg) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().info(msg);
	}

	@Override
	public void log(String msg, Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().info(msg + "\n" + exception);
	}

	@Override
	public void error(String msg, Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().error(msg + "\n" + exception);
	}

	@Override
	public void warn(String msg, Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().warn(msg + "\n" + exception);
	}

	@Override
	public void debug(String msg, Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().debug(msg + "\n" + exception);
	}

	@Override
	public void log(Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().info("" + exception);
	}

	@Override
	public void error(Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().error("" + exception);
	}

	@Override
	public void warn(Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().warn("" + exception);
	}

	@Override
	public void debug(Exception exception) {
		SpongeMinecraftDiscordSync.getInstance().getLogger().debug("" + exception);
	}

}
