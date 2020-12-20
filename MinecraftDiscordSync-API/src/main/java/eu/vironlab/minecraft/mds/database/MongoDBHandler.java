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

package eu.vironlab.minecraft.mds.database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple to use Mongo Database Handler
 * @author depascaldc
 * 
 */
public class MongoDBHandler {

	
    private String databaseName;
    private MongoClient mongoClient;

    /**
     * Create default MongoDBHandler instamce
     * @param plugin
     * @param connectionString
     * @param db_name
     */
    public MongoDBHandler(String connectionString, String databaseName) {
        this.databaseName = databaseName;
        connect(connectionString);
        new Thread(() -> {
    		new Timer().scheduleAtFixedRate(new TimerTask() {
    			@Override
    			public void run() {
    				if(!isConnected()) {
                        try {closeSession();}catch(Exception ex){}
                        connect(connectionString);
                    }
    			}
    		}, 0, 36000);
    	}, "MongoConnectionCheck").start();
    }
    
    /**
     * if you want disable the connection check use this instance
     * @param plugin
     * @param connectionString
     * @param db_name
     * @param checkConnectionEvery3Minutes
     */
    public MongoDBHandler(String connectionString, String databaseName, boolean checkConnectionEvery3Minutes) {
        this.databaseName = databaseName;
        connect(connectionString);
        if(checkConnectionEvery3Minutes)
        	new Thread(() -> {
        		new Timer().scheduleAtFixedRate(new TimerTask() {
        			@Override
        			public void run() {
        				if(!isConnected()) {
                            try {closeSession();}catch(Exception ex){}
                            connect(connectionString);
                        }
        			}
        		}, 0, 36000);
        	}, "MongoConnectionCheck").start();
    }

    /**
     * Build a Document insertable to a collection
     * @param documentName
     * @param keyValueCollection
     * @return new Document
     */
    public Document buildDocument(String documentName, Object[][] keyValueCollection) {
        Document document = new Document("uuid", documentName);
        for (Object[] append : keyValueCollection) {
            document = document.append((String) append[0], append[1]);
        }
        return document;
    }

    /**
     * Add a Document to given collection
     * @param collection
     * @param document
     */
    public void insertDocument(String collection, Document document) {
        mongoClient.getDatabase(databaseName).getCollection(collection).insertOne(document);
    }
    
    /**
     * Replace a Document in given collection with a new Document
     * @param collection
     * @param documentName
     * @param newDocument
     */
    public void replaceDocument(String collection, String documentName, Document newDocument) {
    	getCollection(collection).deleteOne(getDocument(collection, documentName));
        getCollection(collection).insertOne((newDocument));
    }

    /**
     * Add a property to a specified document in a specified collection
     * @param collection
     * @param documentName
     * @param propertiesToAdd
     */
    public void addPropertyToDocument(String collection, String documentName, Object[][] propertiesToAdd) {
        Document toUpdateDocument = getDocument(collection, documentName);
        for (Object[] append : propertiesToAdd) {
            toUpdateDocument.append((String) append[0], append[1]);
        }
        replaceDocument(collection, documentName, toUpdateDocument);
    }

    /**
     * Replace document property with a new value
     * @param collection
     * @param documentName
     * @param property
     * @param newvalue
     */
    public void replaceProperty(String collection, String documentName, String property, Object newvalue) {
        Document document = getDocument(collection, documentName);
        Bson filter = document;
        Document newDocument = new Document("uuid", documentName);
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            if (entry.getKey().equals(property)) {
                if (entry.getKey().equals(document.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), newvalue);
            } else {
                if (entry.getKey().equals(document.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), entry.getValue());
            }
        }
        getCollection(collection).deleteOne(filter);
        getCollection(collection).insertOne((newDocument));
    }
    
    /**
     * Get a new Document object with updated Property
     * @param oldDocument
     * @param property
     * @param newvalue
     * @return newDocument
     */
    public Document replaceProperty(Document oldDocument, String property, Object newvalue) {
        Document newDocument = new Document("uuid", oldDocument.getString("uuid"));
        for (Map.Entry<String, Object> entry : oldDocument.entrySet()) {
            if (entry.getKey().equals(property)) {
                if (entry.getKey().equals(oldDocument.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), newvalue);
            } else {
                if (entry.getKey().equals(oldDocument.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), entry.getValue());
            }
        }
		return newDocument;
    }

    /**
     * Get all Documents in a collection
     * @param collectionName
     * @return FindIterable<Document>
     */
    public FindIterable<Document> getAllDocuments(String collectionName) {
        return getCollection(collectionName).find();
    }
    
    /**
     * Get the count of all documents in a Collection
     * @param collectionName
     * @return long DocumentCount
     */
    @Deprecated
    public long allDocumentsCount(String collectionName) {
    	return getCollection(collectionName).count();
    }

    /**
     * Get a collection out of the database
     * @param collectionName
     * @return MongoCollection<Document> / null
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        if (mongoClient.getDatabase(databaseName).getCollection(collectionName) != null) {
            return mongoClient.getDatabase(databaseName).getCollection(collectionName);
        }
        return null;
    }

    /**
     *  Create a collection in a specified database / collection
     * @param collectionname
     * @param options
     */
    public void createCollection(String collectionname, CreateCollectionOptions options) {
        mongoClient.getDatabase(databaseName).createCollection(collectionname, options);
    }


    /**
     * Get a document out in a specified collection
     * @param collection
     * @param documentName
     * @return Document
     */
    public Document getDocument(String collection, String documentName) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("uuid", documentName);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        return cursor.first();
    }
    
    /**
     * Get a document out in a specified collection by a given key where the value match whereValue
     * @param collection
     * @param byKey
     * @param whereValue
     * @return document
     */
    public Document getDocument(String collection, String byKey, Object whereValue) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(byKey, whereValue);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        return cursor.first();
    }
    
    /**
     * delete a document in a specified collection
     * @param collection
     * @param documentName
     * @return bool
     */
    public boolean deleteOne(String collection, String documentName) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("name", documentName);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        
        getCollection(collection).deleteOne(cursor.first());
        return true;
    }
    
    /**
     * delete a document in a specified collection by a given key where the value match whereValue
     * @param collection
     * @param key
     * @param whereValue
     * @return bool
     */
    public boolean deleteOne(String collection, String key, Object whereValue) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(key, whereValue);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        
        getCollection(collection).deleteOne(cursor.first());
        return true;
    }

    /**
     * Check if the client is Connected to the database server
     * @return boolean
     */
    public boolean isConnected() {
        if (getDatabase(databaseName) != null)
            return true;
        else
            return false;
    }

    /**
     * Let the mongoClient connect to database per given mongoUri
     * @param connectionString
     */
    public void connect(String connectionString) {
        try {
        	mongoClient = new MongoClient(new MongoClientURI(connectionString));
        } catch(Exception ex) {ex.printStackTrace();}
    }

    /**
     * Get a database in MongoDB server
     * @param dbName
     * @return MongoDatabase
     */
    public MongoDatabase getDatabase(String dbName) {
        return mongoClient.getDatabase(dbName);
    }
    
    /**
     * SESSION CLOSIND YOU SHOULD SURROUND WITH TRY/CATCH
     */
    public void closeSession() {
        try { mongoClient.close(); } catch(Exception e) {}
    }

    /**
     * Get the mongoClient
     * @return MongoClient
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

}