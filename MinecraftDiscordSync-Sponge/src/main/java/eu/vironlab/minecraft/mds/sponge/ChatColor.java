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

public enum ChatColor {
    DEFAULT("default", 'r'),
    BLACK("black", '0'),
    DARK_BLUE("dark_blue", '1'),
    GREEN("green", '2'),
    CYAN("cyan", '3'),
    DARK_RED("dark_red", '4'),
    PURPLE("purple", '5'),
    ORANGE("orange", '6'),
    GRAY("gray", '7'),
    DARK_GRAY("dark_gray", '8'),
    BLUE("blue", '9'),
    LIGHT_GREEN("light_green", 'a'),
    AQUA("aqua", 'b'),
    RED("red", 'c'),
    PINK("pink", 'd'),
    YELLOW("yellow", 'e'),
    WHITE("white", 'f');

    private final String name;
    private final char index;

    ChatColor(String name, char index) {
        this.name = name;
        this.index = index;
    }
    
    public static String translateAlternateColorCodes(char c, String text) {
        if (text == null) {
        	return null;
        }
        for (ChatColor consoleColor : values()) {
        	text = text.replace("" + c + consoleColor.index, "§" + consoleColor.index);
        }
        return text;
    }

    public static String stripColorCodes(String text) {
        if (text == null) {
        	return null;
        }

        for (ChatColor consoleColor : values()) {
            text = text.replace("&" + consoleColor.index, "");
            text = text.replace("§" + consoleColor.index, "");
        }

        return text;
    }
    
    public String getName() {
		return name;
	}
    
    public char getIndex() {
		return index;
	}

}
