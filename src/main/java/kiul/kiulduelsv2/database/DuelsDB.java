package kiul.kiulduelsv2.database;

import com.mongodb.*;
import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.duel.Queue;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DuelsDB {
    public static DBCollection duels;
    public static DB kiulnetDB;
    public static MongoClient client;
    public static void connect() {
        // Define your MongoClientOptions with the codec registry
        MongoClientOptions options = MongoClientOptions.builder()
                .retryWrites(true)
                .build();

        // Create the MongoClient instance with the options
        client = new MongoClient(new ServerAddress("localhost", 27017), options);
        //Get the database called "mcserver"
        //If it does not exist it will be created automatically
        //once you save something in it
        kiulnetDB = client.getDB("kiulnet");
        //Get the collection called "players" in the database "mcserver"
        //Equivalent to the table in MySQL, you can store objects in here
        duels = kiulnetDB.getCollection("duels");

    }


    public static void writeGlobal (String objectPath, Object objectToStore) {
        DBObject r = new BasicDBObject("uuid", "server");
        DBObject found = null;
        // try-with-resources will handle the closing of the resources
        try (DBCursor cursor = duels.find(r)){
            while(cursor.hasNext()) {
                found = cursor.next();
            }
        }
        DBObject obj = new BasicDBObject("uuid", "server");
        obj.put(objectPath, objectToStore);

        if (found == null) {
            duels.insert(obj,WriteConcern.SAFE);
            return;
        }

        duels.update(found, obj,true,false,WriteConcern.SAFE);

    }
    public static Object readGlobal(String objectPath){
        //Lets build a minimal object to get all objects in the
        //collection "players" containing the field
        //"uuid" with the value uuid (what we are
        //searching for)
        DBObject r = new BasicDBObject("uuid", "server");
        //Create a cursor object to loop through all the results
        //Lets get all the objects and get the one object that we are searching for
        DBObject found = null;
        // try-with-resources will handle the closing of the resources
        try (DBCursor cursor = duels.find(r)){
            while(cursor.hasNext()) {
                found = cursor.next();
            }
        }

        if (found == null) {
            //User not saved yet. Add him in the DB!
            return null;
        }
        //The user was found! Lets get our values!
        //You can cast the objects to String/Long etc.
        //As they are being delivered as binary objects
        //not as String like in MySQL
        Object object =  found.get(objectPath);
        return object;}
    public static boolean playerExists (UUID uuid) {
        DBObject r = new BasicDBObject("uuid", uuid.toString());
        DBObject found = null;
        boolean exists = false;
        // try-with-resources will handle the closing of the resources
        try (DBCursor cursor = duels.find(r)){
            while(cursor.hasNext()) {
                found = cursor.next();
            }
        }


        if (found == null) {
            return false;
        }
        return true;
    }

    public static void checkIntegrity (UUID uuid) {
        for (String type : Queue.queueTypesLowercase()) {
            if ((readPlayer(uuid, "stat_elo_" + type) == null)) {
                writePlayer(uuid,"stat_elo_"+type,700);
            }
        }
    }
    public static Map<Integer,UUID> getPlacements (String stat) {
        TreeMap<Integer,UUID> placements = new TreeMap<>();
        for (OfflinePlayer offlinePlayers : Bukkit.getOfflinePlayers()) {
            if (placements.size() > 10) {
                break;
            }
            if (readPlayer(offlinePlayers.getUniqueId(),stat+"_placement") == null) {
                continue;
            }
            placements.put((int)readPlayer(offlinePlayers.getUniqueId(),stat+"_placement"),offlinePlayers.getUniqueId());
        }
        return placements;
    }
    public static void setupPlayer (UUID uuid) {
        DBObject obj = new BasicDBObject("uuid", uuid.toString());
        for (String type : Queue.queueTypesLowercase()) {
            obj.put("stat_elo_"+type, 700);
        }
        duels.insert(obj,WriteConcern.SAFE);
        for (String type : Queue.queueTypesLowercase()) {
            updatePlayerPlacement("stat_elo_"+type);
        }
    }
    public static void updatePlayerPlacement (String key) {
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, Integer> map = new HashMap<>();
                LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
                ArrayList<Integer> list = new ArrayList<>();
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if (!key.contains("id")) {
                        key.replaceAll(" ", "");
                        if (readPlayer(offlinePlayer.getUniqueId(), key) == null) {
                            int amount = 0;
                            if (key.contains("elo")) {
                                amount = 700;
                            }
                            writePlayer(offlinePlayer.getUniqueId(), key, amount);
                        }
                        map.put(offlinePlayer.getUniqueId().toString(), (int) readPlayer(offlinePlayer.getUniqueId(), key));
                    }
                }

                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    list.add(entry.getValue());
                }
                Collections.sort(list);
                Collections.reverse(list);
                for (Integer str : list) {
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        if (entry.getValue().equals(str)) {
                            sortedMap.put(entry.getKey(), str);
                        }
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<String> uuids = new ArrayList<>(sortedMap.keySet().stream().toList());
                        for (int i = 0; i < sortedMap.size(); i++) {
                            writePlayer(UUID.fromString(uuids.get(i)), key + "_placement", i + 1);
                        }
                    }
                }.runTask(C.plugin);

            }
        }.runTaskAsynchronously(C.plugin);
    }
    public static Object readPlayer(UUID uuid, String objectPath){
        //Lets build a minimal object to get all objects in the
        //collection "players" containing the field
        //"uuid" with the value uuid (what we are
        //searching for)
        DBObject r = new BasicDBObject("uuid", uuid.toString());
        //Create a cursor object to loop through all the results
        //Lets get all the objects and get the one object that we are searching for
        DBObject found = null;
        // try-with-resources will handle the closing of the resources
        try (DBCursor cursor = duels.find(r)){
            while(cursor.hasNext()) {
                found = cursor.next();
            }
        }

        if (found == null) {
            //User not saved yet. Add him in the DB!
            return null;
        }
        //The user was found! Lets get our values!
        //You can cast the objects to String/Long etc.
        //As they are being delivered as binary objects
        //not as String like in MySQL
        Object object =  found.get(objectPath);
        return object;}
    public static Set<String> returnKeys(UUID uuid){
        //Lets build a minimal object to get all objects in the
        //collection "players" containing the field
        //"uuid" with the value uuid (what we are
        //searching for)
        DBObject r = new BasicDBObject("uuid", uuid.toString());
        //Create a cursor object to loop through all the results
        //Lets get all the objects and get the one object that we are searching for
        DBObject found = null;
        // try-with-resources will handle the closing of the resources
        try (DBCursor cursor = duels.find(r)){
            while(cursor.hasNext()) {
                found = cursor.next();
            }
        }

        if (found == null) {
            //User not saved yet. Add him in the DB!
            return null;
        }
        //The user was found! Lets get our values!
        //You can cast the objects to String/Long etc.
        //As they are being delivered as binary objects
        //not as String like in MySQL
        return found.keySet();
    }
    public static void writePlayer (UUID uuid, String objectPath, Object objectToStore) {
        new BukkitRunnable() {
            @Override
            public void run() {


                // Query for the document with the specified UUID
                DBObject query = new BasicDBObject("uuid", uuid.toString());
                DBObject found = duels.findOne(query);

                // Create a new document with the UUID and specified object
                DBObject document = new BasicDBObject("uuid", uuid.toString());
                document.put(objectPath, objectToStore);

                // If an existing document is found, update it; otherwise, insert a new document

                if (found != null) {
                    // Update the existing document with the new values
                    DBObject update = new BasicDBObject("$set", document);
                    duels.update(found, update);
                } else {
                    // Insert a new document with the specified UUID and object
                    duels.insert(document);
                }
                if (objectPath.contains("stat") && !objectPath.contains("placement")) {
                    updatePlayerPlacement(objectPath);
                }
            }
        }.runTaskAsynchronously(C.plugin);
    }
}
