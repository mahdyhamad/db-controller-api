package Collection;


import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Collection {
    String name;
    CollectionController controller;

    public Collection(String name, String path){
        this.name = name;
        this.controller = new CollectionController(name, path);
    }

    public String getName() {
        return name;
    }

    public boolean createIndex(String field){
        return this.controller.createIndex(field);
    }

    public boolean deleteIndex(String field){
        return this.controller.deleteIndex(field);
    }

    public ArrayList<JSONObject> find(String field, String value){
        return controller.getDocument(field, value);
    }
}
