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

package eu.vironlab.minecraft.mds.velocity.configuration;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class Utils {
	
	public static void writeFile(String fileName, String content) throws IOException {
		writeFile(fileName, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
	}

	public static void writeFile(String fileName, InputStream content) throws IOException {
		writeFile(new File(fileName), content);
	}

	public static void writeFile(File file, String content) throws IOException {
		writeFile(file, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
	}

	public static void writeFile(File file, InputStream content) throws IOException {
		if (content == null) {
			throw new IllegalArgumentException("content must not be null");
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		try (FileOutputStream stream = new FileOutputStream(file)) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = content.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
		}
		content.close();
	}

	public static String readFile(File file) throws IOException {
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		return readFile(new FileInputStream(file));
	}

	public static String readFile(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		return readFile(new FileInputStream(file));
	}

	public static String readFile(InputStream inputStream) throws IOException {
		return readFile(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}

	private static String readFile(Reader reader) throws IOException {
		try (BufferedReader br = new BufferedReader(reader)) {
			String temp;
			StringBuilder stringBuilder = new StringBuilder();
			temp = br.readLine();
			while (temp != null) {
				if (stringBuilder.length() != 0) {
					stringBuilder.append("\n");
				}
				stringBuilder.append(temp);
				temp = br.readLine();
			}
			return stringBuilder.toString();
		}
	}

	public static void copyFile(File from, File to) throws IOException {
		if (!from.exists()) {
			throw new FileNotFoundException();
		}
		if (from.isDirectory() || to.isDirectory()) {
			throw new FileNotFoundException();
		}
		FileInputStream fi = null;
		FileChannel in = null;
		FileOutputStream fo = null;
		FileChannel out = null;
		try {
			if (!to.exists()) {
				to.createNewFile();
			}
			fi = new FileInputStream(from);
			in = fi.getChannel();
			fo = new FileOutputStream(to);
			out = fo.getChannel();
			in.transferTo(0, in.size(), out);
		} finally {
			if (fi != null)
				fi.close();
			if (in != null)
				in.close();
			if (fo != null)
				fo.close();
			if (out != null)
				out.close();
		}
	}

}
