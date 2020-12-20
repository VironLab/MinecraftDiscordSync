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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.security.auth.login.LoginException;

import eu.vironlab.minecraft.mds.discordbot.command.*;
import eu.vironlab.minecraft.mds.discordbot.events.DiscordCommandEvent;
import eu.vironlab.minecraft.mds.logging.IPluginLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public abstract class DiscordBot implements IDiscordBot {
	
	private JDA jda;
	protected String token;
	
	private IPluginLogger pluginLogger;
	
	private String prefix;
	
	private boolean started = false;
	
	private static List<Runnable> onInterruptRunnables;
	
	private List<ListenerAdapter> listenerAdaptersToAdd;
	
	private DiscordCommandMap commandMap;
	
	public DiscordBot(IPluginLogger pluginLogger, String token, String commandPrefix) {
		this.token = token;
		this.prefix = commandPrefix;
		this.pluginLogger = pluginLogger;
		onInterruptRunnables = new ArrayList<Runnable>();
		listenerAdaptersToAdd = new ArrayList<ListenerAdapter>();
		commandMap = new DiscordCommandMap();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for(Runnable run : onInterruptRunnables) {
				try {
					run.run();
				} catch(Exception e) {
					pluginLogger.error(e);
				}
			}
		}));
		setupOtherLoggers();
	}
	
	private static void setupOtherLoggers() {
	}
	
	@Override
	public boolean startBot() {
		JDABuilder builder = JDABuilder.createDefault(token, EnumSet.allOf(GatewayIntent.class));
		builder.addEventListeners(new DiscordCommandEvent(prefix, this));
		builder.setAutoReconnect(true);
		for(ListenerAdapter adapter : listenerAdaptersToAdd)
			builder.addEventListeners(adapter);
		try {
			jda = builder.build();
		} catch(LoginException e) {
			return false;
		} catch (Exception e) {
		}
		return started = true;
	}
	
	@Override
	public boolean addCommand(DiscordCommand... commands) {
		for(DiscordCommand command : commands) {
			try {
				commandMap.registerCommand(command);
			} catch (Exception e) {
			}
		}
		return true;
	}
	
	public boolean addListenerBeforeStart(ListenerAdapter adapter) {
		listenerAdaptersToAdd.add(adapter);
		return true;
	}
	
	@Override
	public boolean addListener(ListenerAdapter adapter) {
		try {
			jda.addEventListener(adapter);
		} catch (Exception e) {
			pluginLogger.error(e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean addListeners(Object...adapters) {
		try {
			jda.addEventListener(adapters);
		} catch (Exception e) {
			pluginLogger.error(e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean stopBot() {
		if(this.getJDA() != null)
			jda.shutdownNow();
		jda = null;
		started = false;
		return true;
	}
	
	@Override
	public boolean addShutDownHook(Runnable runnable) {
		return onInterruptRunnables.add(runnable);
	}
	
	@Override
	public void interrupt() {
		for(Runnable run : onInterruptRunnables) {
			try {
				run.run();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		stopBot();
	}
	
	@Override
	public JDA getJDA() {
		return jda;
	}
	
	@Override
	public IPluginLogger getPluginLogger() {
		return pluginLogger;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public DiscordCommandMap getCommandMap() {
		return commandMap;
	}
	
	@Override
	public String getPrefix() {
		return prefix;
	}
	
	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
