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

package eu.vironlab.minecraft.mds.discordbot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import eu.vironlab.minecraft.mds.discordbot.IDiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.internal.utils.Checks;

public class CommandData {

	public static int MAX_MESSAGES = 2;

	private MessageReceivedEvent event;
	private List<String> args;

	private IDiscordBot client;

	public CommandData(MessageReceivedEvent event, IDiscordBot client, List<String> args) {
		this.event = event;
		this.args = args == null ? new ArrayList<String>() : args;
		this.client = client;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public MessageReceivedEvent getEvent() {
		return event;
	}

	public IDiscordBot getClient() {
		return client;
	}

	public IDiscordBot getBot() {
		return client;
	}

	public void reply(String message) {
		sendMessage(event.getChannel(), message);
	}

	public void reply(String message, Consumer<Message> success) {
		sendMessage(event.getChannel(), message, success);
	}

	public void reply(String message, Consumer<Message> success, Consumer<Throwable> failure) {
		sendMessage(event.getChannel(), message, success, failure);
	}

	public void replyInDm(String message) {
		if (event.isFromType(ChannelType.PRIVATE))
			reply(message);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> sendMessage(pc, message));
		}
	}

	public void replyInDm(String message, Consumer<Message> success) {
		if (event.isFromType(ChannelType.PRIVATE))
			reply(message, success);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> sendMessage(pc, message, success));
		}
	}

	public void replyInDm(String message, Consumer<Message> success, Consumer<Throwable> failure) {
		if (event.isFromType(ChannelType.PRIVATE))
			reply(message, success, failure);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> sendMessage(pc, message, success, failure), failure);
		}
	}

	public void replyEmbedInDm(MessageEmbed embed) {
		if (event.isFromType(ChannelType.PRIVATE))
			reply(embed);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue());
		}
	}

	public void replyEmbedInDm(MessageEmbed embed, Consumer<Message> success) {
		if (event.isFromType(ChannelType.PRIVATE))
			getPrivateChannel().sendMessage(embed).queue(success);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue(success));
		}
	}

	public void replyEmbedInDm(MessageEmbed embed, Consumer<Message> success, Consumer<Throwable> failure) {
		if (event.isFromType(ChannelType.PRIVATE))
			getPrivateChannel().sendMessage(embed).queue(success, failure);
		else {
			event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(embed).queue(success, failure), failure);
		}
	}

	public void reply(MessageEmbed embed) {
		event.getChannel().sendMessage(embed).queue();
	}

	public void reply(MessageEmbed embed, Consumer<Message> success) {
		event.getChannel().sendMessage(embed).queue(m -> {
			success.accept(m);
		});
	}

	public void reply(MessageEmbed embed, Consumer<Message> success, Consumer<Throwable> failure) {
		event.getChannel().sendMessage(embed).queue(m -> {
			success.accept(m);
		}, failure);
	}

	public void reactSuccess() {
		react(":white_checkmark:");
	}

	public void reactWarning() {
		react(":X:");
	}

	public void reactError() {
		react(":X:");
	}

	public void async(Runnable runnable) {
		Checks.notNull(runnable, "Runnable");
		new Thread(runnable).run();
	}

	private void react(String reaction) {
		if (reaction == null || reaction.isEmpty())
			return;
		try {
			event.getMessage().addReaction(reaction.replaceAll("<a?:(.+):(\\d+)>", "$1:$2")).queue();
		} catch (PermissionException ignored) {
		}
	}

	private void sendMessage(MessageChannel chan, String message) {
		ArrayList<String> messages = splitMessage(message);
		for (int i = 0; i < MAX_MESSAGES && i < messages.size(); i++) {
			chan.sendMessage(messages.get(i)).queue();
		}
	}

	private void sendMessage(MessageChannel chan, String message, Consumer<Message> success) {
		ArrayList<String> messages = splitMessage(message);
		for (int i = 0; i < MAX_MESSAGES && i < messages.size(); i++) {
			if (i + 1 == MAX_MESSAGES || i + 1 == messages.size()) {
				chan.sendMessage(messages.get(i)).queue(m -> {
					success.accept(m);
				});
			} else {
				chan.sendMessage(messages.get(i)).queue();
			}
		}
	}

	private void sendMessage(MessageChannel chan, String message, Consumer<Message> success,
			Consumer<Throwable> failure) {
		ArrayList<String> messages = splitMessage(message);
		for (int i = 0; i < MAX_MESSAGES && i < messages.size(); i++) {
			if (i + 1 == MAX_MESSAGES || i + 1 == messages.size()) {
				chan.sendMessage(messages.get(i)).queue(m -> {
					success.accept(m);
				}, failure);
			} else {
				chan.sendMessage(messages.get(i)).queue();
			}
		}
	}

	public static ArrayList<String> splitMessage(String stringtoSend) {
		ArrayList<String> msgs = new ArrayList<String>();
		if (stringtoSend != null) {
			stringtoSend = stringtoSend.replace("@everyone", "@\u0435veryone").replace("@here", "@h\u0435re").trim();
			while (stringtoSend.length() > 2000) {
				int leeway = 2000 - (stringtoSend.length() % 2000);
				int index = stringtoSend.lastIndexOf("\n", 2000);
				if (index < leeway)
					index = stringtoSend.lastIndexOf(" ", 2000);
				if (index < leeway)
					index = 2000;
				String temp = stringtoSend.substring(0, index).trim();
				if (!temp.equals(""))
					msgs.add(temp);
				stringtoSend = stringtoSend.substring(index).trim();
			}
			if (!stringtoSend.equals(""))
				msgs.add(stringtoSend);
		}
		return msgs;
	}

	public SelfUser getSelfUser() {
		return event.getJDA().getSelfUser();
	}

	public Member getSelfMember() {
		return event.getGuild() == null ? null : event.getGuild().getSelfMember();
	}

	public User getAuthor() {
		return event.getAuthor();
	}

	public MessageChannel getChannel() {
		return event.getChannel();
	}

	public ChannelType getChannelType() {
		return event.getChannelType();
	}

	public Guild getGuild() {
		return event.getGuild();
	}

	public JDA getJDA() {
		return event.getJDA();
	}

	public Message getMessage() {
		return event.getMessage();
	}

	public PrivateChannel getPrivateChannel() {
		return event.getPrivateChannel();
	}

	public long getResponseNumber() {
		return event.getResponseNumber();
	}

	public TextChannel getTextChannel() {
		return event.getTextChannel();
	}

	public boolean isFromType(ChannelType channelType) {
		return event.isFromType(channelType);
	}

}
