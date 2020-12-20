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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public abstract class MessageProvider implements IMessageProvider {

	private static Map<String, String> lang;
	private static Map<String, String> fallbackLang;

	@Override
	public void load(File pluginFolder) {
		try {
			lang = loadLang(new FileInputStream(new File(pluginFolder, "messages.ini")));
		} catch (Exception e) {
			lang = new HashMap<String, String>();
		}
		fallbackLang = loadLang(MessageProvider.class.getClassLoader().getResourceAsStream("defaults.ini"));
	}
	
	@Override
	public abstract String translate(String str, Object... params);
	@Override
	public abstract String translate(String str, String... params);

	private static Map<String, String> loadLang(InputStream stream) {
		try {
			String content = IOUtils.toString(stream, Charset.forName("utf-8"));
			Map<String, String> d = new HashMap<String, String>();
			for (String line : content.split("\n")) {
				line = line.trim();
				if (line.equals("") || line.charAt(0) == '#') {
					continue;
				}
				String[] t = line.split("=");
				if (t.length < 2) {
					continue;
				}
				String key = t[0];
				StringBuilder value = new StringBuilder();
				for (int i = 1; i < t.length - 1; i++) {
					value.append(t[i]).append("=");
				}
				value.append(t[t.length - 1]);
				if (value.toString().equals("")) {
					continue;
				}
				d.put(key, value.toString());
			}
			return d;
		} catch (IOException e) {
			return null;
		}
	}

	protected String internalGet(String id) {
		if (lang.containsKey(id)) {
			return lang.get(id);
		} else if (fallbackLang.containsKey(id)) {
			return fallbackLang.get(id);
		}
		return null;
	}

	protected String get(String id) {
		if (lang.containsKey(id)) {
			return lang.get(id);
		} else if (fallbackLang.containsKey(id)) {
			return fallbackLang.get(id);
		}
		return id;
	}

	@Override
	public String parseMessage(String text) {
		StringBuilder newString = new StringBuilder();
		text = String.valueOf(text);
		StringBuilder replaceString = null;
		int len = text.length();
		for (int i = 0; i < len; ++i) {
			char c = text.charAt(i);
			if (replaceString != null) {
				int ord = c;
				if ((ord >= 0x30 && ord <= 0x39) || (ord >= 0x41 && ord <= 0x5a) || (ord >= 0x61 && ord <= 0x7a)
						|| c == '.' || c == '-') {
					replaceString.append(String.valueOf(c));
				} else {
					String t = internalGet(replaceString.substring(1));
					if (t != null) {
						newString.append(t);
					} else {
						newString.append(replaceString);
					}
					replaceString = null;
					if (c == '%') {
						replaceString = new StringBuilder(String.valueOf(c));
					} else {
						newString.append(String.valueOf(c));
					}
				}
			} else if (c == '%') {
				replaceString = new StringBuilder(String.valueOf(c));
			} else {
				newString.append(String.valueOf(c));
			}
		}
		if (replaceString != null) {
			String t = internalGet(replaceString.substring(1));
			if (t != null) {
				newString.append(t);
			} else {
				newString.append(replaceString);
			}
		}
		return newString.toString();
	}

}
