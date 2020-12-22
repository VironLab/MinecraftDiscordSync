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

package eu.vironlab.minecraft.mds.velocity;

import eu.vironlab.minecraft.mds.MessageProvider;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class Messages extends MessageProvider {

	@Override
	public String translate(String str, Object... params) {
		String baseText = get(str);
		baseText = parseMessage(baseText != null ? baseText : str);
		for (int i = 0; i < params.length; i++) {
			baseText = baseText.replace("{%" + i + "}", parseMessage(String.valueOf(params[i])));
		}
		return translateColor(baseText);
	}

	@Override
	public String translate(String str, String... params) {
		String baseText = get(str);
		baseText = parseMessage(baseText != null ? baseText : str);
		for (int i = 0; i < params.length; i++) {
			baseText = baseText.replace("{%" + i + "}", parseMessage(params[i]));
		}
		return translateColor(baseText);
	}
	
	public Component getAsTextComponent(String str, Object... params) {
		return LegacyComponentSerializer.legacy().deserialize(translate(str, params));
	}
	
	public Component getAsTextComponent(String str, String... params) {
		return LegacyComponentSerializer.legacy().deserialize(translate(str, params));
	}
	
	public static String translateColor(String str) {
		return LegacyComponentSerializer.legacy().serialize(LegacyComponentSerializer.legacy().deserialize(str, '&'));
	}
	
	public static String stripColor(String str) {
		return ChatColor.stripColorCodes(str);
	}
	
	public static Component buildComponent(String str) {
		return LegacyComponentSerializer.legacy().deserialize(translateColor(str));
	}

}
