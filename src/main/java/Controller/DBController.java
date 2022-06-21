package Controller;

import Database.Database;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

public class DBController {
    Vector<Database> databases;
    public DBController(){
        // load all databases
        databases = new Vector<>();
        System.out.println("Loading Databases...");
        this.loadDatabase();
        System.out.println("Database is up and running");

    }

    public boolean loadDatabase(){
        this.databases.clear();
        try{
            String dataPath = "./db/data/";
            File dataDir = new File(dataPath);
            for (File file: Objects.requireNonNull(dataDir.listFiles())){
                Database db = new Database(file.getName());
                databases.add(db);
            }
        }
        catch (Exception e){
            System.out.println("An exception occurred while loading data: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Database getDatabase(String name){
        // return database by its name
        for (Database database: databases){
            if (Objects.equals(database.getName(), name))
                return database;
        }
        return null;
    }

}
