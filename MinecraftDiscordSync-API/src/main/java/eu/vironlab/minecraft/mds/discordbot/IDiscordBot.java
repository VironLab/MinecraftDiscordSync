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

package eu.vironlab.minecraft.mds.discordbot;

import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommand;
import eu.vironlab.minecraft.mds.discordbot.command.DiscordCommandMap;
import eu.vironlab.minecraft.mds.logging.IPluginLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public interface IDiscordBot {
	
	/**
	 * Start the DiscordBot
	 * @return started
	 */
	public boolean startBot();
	
	/**
	 * Actions wich should run when you stop the bot when the interrupt(); method id called.
	 * @param runnable
	 * @return
	 */
	public boolean addShutDownHook(Runnable runnable);
	
	/**
	 * Add a listener to the JDA instance
	 * @param adapter
	 * @return success
	 */
	public boolean addListener(ListenerAdapter adapter);
	
	/**
	 * Add more listeners to the JDA instance
	 * @param adapters
	 * @return success
	 */
	public boolean addListeners(Object...adapters);
	
	/**
	 * Extend the DiscordCommandMap
	 * @param commands
	 * @return success
	 */
	public boolean addCommand(DiscordCommand...commands);
	
	/**
	 * Get the DiscordCommandMap wich includes add DiscordCommands
	 * @return commandMap
	 */
	public DiscordCommandMap getCommandMap();
	
	/**
	 * Get the Bots JDA instance
	 * @return jdaInstance
	 */
	public JDA getJDA();
	
	/**
	 * Stop the DiscordBot
	 * @return stopped
	 */
	public boolean stopBot();
	
	/**
	 * Destroy the client
	 */
	public void interrupt();
	
	/**
	 * Get the plugin logger
	 * @return
	 */
	public IPluginLogger getPluginLogger();
	
	public String getPrefix();
	public void setPrefix(String prefix);

}
