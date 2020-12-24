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

package eu.vironlab.minecraft.mds.dependency.clazzloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class RefClassLoader implements IPluginClassLoader {

	private static Method addUrl;

	private URLClassLoader classLoader;

	static {
		try {
			openUrlClassLoaderModule();
		} catch (Throwable throwable) {}
		try {
			addUrl = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			addUrl.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public RefClassLoader() {
		if (getClass().getClassLoader() instanceof URLClassLoader) {
			this.classLoader = (URLClassLoader) getClass().getClassLoader();
		} else {
			throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
		}
	}

	@Override
	public void addJarToClasspath(File paramPath) {
		try {
			addUrl.invoke(this.classLoader, new Object[] { paramPath.toURI().toURL() });
		} catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void openUrlClassLoaderModule() throws Exception {
		Class<?> moduleClass = Class.forName("java.lang.Module");
		Method getModuleMethod = Class.class.getMethod("getModule", new Class[0]);
		Method addOpensMethod = moduleClass.getMethod("addOpens", new Class[] { String.class, moduleClass });
		Object urlClassLoaderModule = getModuleMethod.invoke(URLClassLoader.class, new Object[0]);
		Object thisModule = getModuleMethod.invoke(RefClassLoader.class, new Object[0]);
		addOpensMethod.invoke(urlClassLoaderModule,
				new Object[] { URLClassLoader.class.getPackage().getName(), thisModule });
	}

}
