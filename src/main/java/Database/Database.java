package Database;

import Config.Configuration;
import Collection.Collection;

import java.io.File;
import java.sql.Array;
import java.util.Objects;
import java.util.ArrayList;

public class Database {
    String name;
    ArrayList<Collection> collections;

    public Database(String name){
        this.name = name;
        collections = new ArrayList<>();
        String databasePath = Configuration.DATA_PATH + name + "/";
        File databaseDir = new File(databasePath);
        for (File file: Objects.requireNonNull(databaseDir.listFiles())){
            String collectionPath = databasePath + file.getName();
            Collection collection = new Collection(file.getName(), collectionPath);
            collections.add(collection);
        }
    }

    public String getName() {
        return name;
    }

    public Collection getCollection(String name){
        for (Collection collection: collections){
            System.out.println(collection.getName());
            if ((name + ".json").equals(collection.getName()))
                return collection;
        }
        return null;
    }

    public Collection createCollection(String name){
        // TODO: implement functionality
        return null;
    }

    public Boolean deleteCollection(String name){
        // TODO: implement functionality
        return false;
    }
}
