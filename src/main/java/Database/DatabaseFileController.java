package Database;

import java.io.File;
import Config.Configuration;

public class DatabaseFileController {
    public static String getDatabaseDir(String name){
        return Configuration.DATA_PATH + name + "/";
    }
    public static boolean databaseExists(String name){
        File dbDirectory = new File(getDatabaseDir(name));
        return dbDirectory.exists();
    }

    public static void createDatabase(String name){
        if (!databaseExists(name)){
            File dbDirectory = new File(getDatabaseDir(name));
            dbDirectory.mkdir();
        }
        else{
            System.out.println("Database with name: " + name + " already exists");
        }
    }

    public static void renameDatabase(String name, String newName){
        boolean originalDBNameExists = databaseExists(name);
        boolean newDBNameExists = databaseExists(newName);
        String msg = "";
        if (originalDBNameExists && !newDBNameExists){
            File dbDirectory = new File(getDatabaseDir(name));
            dbDirectory.mkdir();
        } else if (!originalDBNameExists) {
            msg = "No database with name: " + name + " does not exist";
        } else{
            msg = "Database with name: " + name + " already exists";
        }
        if (!msg.equals("")){
            System.out.println(msg);
        }
    }

    public static void deleteDatabase(String name){

    }


}
