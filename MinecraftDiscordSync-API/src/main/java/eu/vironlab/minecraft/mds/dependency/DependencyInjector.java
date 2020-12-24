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

package eu.vironlab.minecraft.mds.dependency;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import eu.vironlab.minecraft.mds.dependency.clazzloader.IPluginClassLoader;

public class DependencyInjector {
	
	private File dataPath;
	private static final String SUFFIX = ".jar";
	
	private IPluginClassLoader clazzLoader;
	
	public DependencyInjector(File dataPath, IPluginClassLoader clazzLoader) {
		this.dataPath = dataPath;
		this.clazzLoader = clazzLoader;
	}
	
    public void require(String coords) {
        require(null, coords);
    }

    public void require(String server, String coords) {
        String[] split = coords.split(":");
        if (split.length != 3) {
            System.out.println("Wrong Library input... StringExample: 'groupid:artifactid:version' Given: '" + coords + "'");
        }

        load(new IDependency() {
            public String getGroup() {
                return split[0];
            }

            public String getName() {
                return split[1];
            }

            public String getVersion() {
                return split[2];
            }
        }, server);

    }
    
    public void loadJDA() {
    	String jdaDirectDownload = "https://github.com/DV8FromTheWorld/JDA/releases/download/v4.2.0/JDA-4.2.0_168-withDependencies-min.jar";
    	try {
            File dest = new File(dataPath, "JDA-with-dependencies.jar");
            if (!dest.exists()) {
                System.out.println("[MCDCS] »» Downloading library JDA !");
                dest.getParentFile().mkdirs();
                URL requestURL = new URL(jdaDirectDownload);
                Files.copy(requestURL.openStream(), dest.toPath());
            }
        	try {
        		clazzLoader.addJarToClasspath(dest);
            } catch (Exception ex) {
                throw new DependencyLoadException("JDA-4.2.0_168");
            }
		} catch (Exception e) {
			throw new DependencyLoadException("JDA-4.2.0_168");
		}
    }

    private void load(IDependency library, String server) {
        if (server == null) server = "https://repo1.maven.org/maven2/";
        
        if(!server.endsWith("/")) server = server + "/";

        String filePath = library.getGroup().replace('.', '/') + '/' + library.getName() + '/' + library.getVersion();
        String fileName = library.getName() + '-' + library.getVersion() + SUFFIX;

        File folder = new File(dataPath, filePath);
        File dest = new File(folder, fileName);

        try {
        	if (!dest.exists()) {
                System.out.println("[MCDCS] »» Downloading library " + fileName + " !");
                dest.getParentFile().mkdirs();
                URL requestURL = new URL(server + filePath + "/" + fileName);
                Files.copy(requestURL.openStream(), dest.toPath());
            }
        	try {
        		clazzLoader.addJarToClasspath(dest);
            } catch (Exception ex) {
            	ex.printStackTrace();return;
                //throw new DependencyLoadException(library);
            }
		} catch (Exception e) {
			e.printStackTrace();return;
			//throw new DependencyLoadException(library);
		}

        System.out.println("[MCDCS] »» Loading library " + fileName + " done!");
    }


	public static boolean classExists(String clazzLocation) {
		try {
			Class.forName(clazzLocation);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
}
