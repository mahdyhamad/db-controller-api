package Database;

import Collection.Collection;
import Config.Configuration;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

public class Database {
    String name;
    Vector<Collection> collections;

    public Database(String name){
        this.name = name;
        collections = new Vector<>();
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
