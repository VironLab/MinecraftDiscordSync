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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;

import eu.vironlab.minecraft.mds.database.MongoDBHandler;
import eu.vironlab.minecraft.mds.verification.VerifiedUser;
import okhttp3.internal.http2.ConnectionShutdownException;

public class MongoDBStorage implements IStorageProvider {

	private final String collection = "mcdcs_verified";

	private MongoDBHandler db;

	private Map<UUID, Document> userDocs = new HashMap<UUID, Document>();

	@Override
	public void connect(String user, String host, String password, int port, String dbName) throws ConnectionShutdownException {
		db = new MongoDBHandler(
				"mongodb://" + user + ":" + password + "@" + host + ":" + port != null ? Integer.toString(port)
						: "27017" + "/" + dbName,
				dbName);
		if(!db.isConnected()) {
			throw new ConnectionShutdownException();
		}
	}

	@Override
	public void disconnect() {
		try {
			db.closeSession();
		} catch (Exception e) {
		}
	}

	@Override
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID, boolean verified) {
		Document userDocument = getUserDocument(minecraftUUID);
		if (userDocument.getBoolean("verified") != verified) {
			userDocument.put("verified", verified);
			db.replaceProperty(collection, minecraftUUID.toString(), "verified", verified);
			userDocs.put(minecraftUUID, userDocument);
		}
		return true;
	}

	@Override
	public boolean addVerifiedUser(String discordID, UUID minecraftUUID) {
		return addVerifiedUser(discordID, minecraftUUID, true);
	}

	@Override
	public boolean removeVerifiedUser(String discordID) {
		try {
			Document userDocument = getUserDocument(discordID);
			if(userDocument != null) {
				UUID minecraftUUID = UUID.fromString(userDocument.getString("uuid"));
				return removeVerifiedUser(minecraftUUID);
			} else return true;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean removeVerifiedUser(UUID minecraftUUID) {
		try {
			db.deleteOne(collection, minecraftUUID.toString());
		} catch (Exception e) {
		}
		try {
			userDocs.remove(minecraftUUID);
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public VerifiedUser getVerifiedUserObj(String discordID) {
		Document userDocument = getUserDocument(discordID);
		return new VerifiedUser(UUID.fromString(userDocument.getString("uuid")), discordID, userDocument.getBoolean("verified", false), this);
	}

	@Override
	public VerifiedUser getVerifiedUserObj(UUID minecraftUUID) {
		Document userDocument = getUserDocument(minecraftUUID);
		return new VerifiedUser(minecraftUUID, userDocument.getString("discord_id"), userDocument.getBoolean("verified", false), this);
	}

	private Document getUserDocument(UUID minecraftUUID) {
		Document userDocument;
		if (!userDocs.containsKey(minecraftUUID)) {
			userDocument = db.getDocument(collection, minecraftUUID.toString());
			if (userDocument == null) {
				userDocument = db.buildDocument(minecraftUUID.toString(),
						new Object[][] { { "discord_id", "none" }, { "verified", false } });
			}
		} else
			userDocument = userDocs.get(minecraftUUID);
		return userDocument;
	}

	private Document getUserDocument(String discordID) {
		Document userDocument = db.getDocument(collection, "discord_id", discordID);
		userDocs.put(UUID.fromString(userDocument.getString("uuid")), userDocument);
		return userDocument;
	}

}
