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

package eu.vironlab.minecraft.mds.storage;

import java.util.UUID;

import eu.vironlab.minecraft.mds.verification.VerifiedUser;
import okhttp3.internal.http2.ConnectionShutdownException;

public interface IStorageProvider {
	
	/**
	 * Connect the Storage provider (Database or local file or whatever)
	 * @param user
	 * @param host
	 * @param password
	 * @param port
	 * @param dbName <br>Should be the AuthenticationDatabase (If nothing is provided 
	 * {@link eu.vironlab.minecraft.mds.database.MongoDBHandler } will choose the default database "admin" ) 
	 * for every other database handler should that be specified too or handled otherways
	 */
	public void connect(String user, String host, String password, int port, String dbName) throws ConnectionShutdownException;
	/**
	 * Disconnect the storage provider ( cut database connections )
	 */
	public void disconnect();
	
	/**
	 * add a new verified user 
	 * @param discordID
	 * @param minecraftUUID
	 * @param verified
	 * @return success
	 */
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID, boolean verified);
	/**
	 * add a new verified user ( verified = true )
	 * @param discordID
	 * @param minecraftUUID
	 * @return success
	 */
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID);
	/**
	 * remove a verified user by discordID
	 * @param discordID
	 * @return done
	 */
	public boolean removeVerifiedUser(String discordID);
	/**
	 * remove a verified user by mineraftUUID
	 * @param minecraftUUID
	 * @return done
	 */
	public boolean removeVerifiedUser(UUID minecraftUUID);
	
	/**
	 * Resolve VerifiedUser from storage
	 * @param discordID
	 * @return verifiedUser
	 */
	public VerifiedUser getVerifiedUserObj(String discordID);
	/**
	 * Resolve VerifiedUser from storage
	 * @param minecraftUUID
	 * @return verifiedUser
	 */
	public VerifiedUser getVerifiedUserObj(UUID minecraftUUID);

}
