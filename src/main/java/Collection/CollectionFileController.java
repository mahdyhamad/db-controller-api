package Collection;

import Config.PathManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class CollectionFileController {

    private String collectionPath;

    public static boolean collectionExists(String collectionPath){
        File collectionFile = new File(collectionPath);
        return collectionFile.exists();
    }
    public static boolean collectionExists(String db, String collection){
        String collectionPath = PathManager.getCollectionPath(db, collection);
        return collectionExists(collectionPath);
    }

    public static void createCollection(String db, String collection) throws IOException {
        String collectionPath = PathManager.getCollectionPath(db, collection);
        boolean collectionExists = collectionExists(collectionPath);
        if (!collectionExists){
            JSONObject rootObject = new JSONObject();
            FileWriter file = new FileWriter(collectionPath);
            JSONArray initial = new JSONArray();
            rootObject.put("documents", initial);
            initial.add("_id");
            rootObject.put("indexes", initial);
            file.write(rootObject.toJSONString());
            file.flush();
        }
    }

    public static void renameCollection(String db, String collection, String updatedName){
        String collectionPath = PathManager.getCollectionPath(db, collection);
        File collectionDir = new File(collectionPath);

        assert collectionDir.isDirectory(): "Collection does not exist";
        File newCollectionDir = new File(collectionDir.getParent() + "/" + updatedName + ".json");
        collectionDir.renameTo(newCollectionDir);
    }
    public static void updateCollection(String db, String collection, HashMap<String, Object> data){

    }

    public static void deleteCollection(String db, String collection){
        String collectionPath = PathManager.getCollectionPath(db, collection);
        File collectionDir = new File(collectionPath);
        if (collectionDir.delete()){
            System.out.println("Collection " + collection + " deleted");
        }
    }

    private static JSONArray getDocuments(String collectionPath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        return (JSONArray) collection.getOrDefault("documents", new JSONArray());
    }

    private static void writeDocumentsToCollection(String collectionPath, JSONArray documents) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        FileWriter file = new FileWriter(collectionPath);
        collection.put("documents", documents);
        file.write(collection.toJSONString());
        file.flush();
    }


    private static boolean documentExists(JSONArray documents, String id){
        for (Object document : documents) {
            JSONObject o = (JSONObject) document;
            if (o.get("_id") == id) {
                return true;
            }
        }
        return false;
    }

    public static void addDocument(String db, String collection, Map<String, Object> data) throws IOException, ParseException {
        String collectionPath = PathManager.getCollectionPath(db, collection);
        JSONArray documents = getDocuments(collectionPath);
        String _id = UUID.randomUUID().toString();
        JSONObject newDocument = new JSONObject(data);
        newDocument.put("_id", _id);
        documents.add(newDocument);
        writeDocumentsToCollection(collectionPath, documents);
    }

    public static void deleteDocument(String db, String collection, String id) throws IOException, ParseException {
        String collectionPath = PathManager.getCollectionPath(db, collection);
        JSONArray documents = getDocuments(collectionPath);
        int documentIndex = -1;
        for (int i=0; i < documents.size(); i++){
            JSONObject document = (JSONObject) documents.get(i);
            if (Objects.equals(document.get("_id").toString(), id)){
                documentIndex = i;
                break;
            }
        }
        if (documentIndex != -1){
            documents.remove(documentIndex);
            writeDocumentsToCollection(collectionPath, documents);
        }
    }

    public static void updateDocument(String db, String collection, String id, HashMap<String, Object> data) throws IOException, ParseException {
        String collectionPath = PathManager.getCollectionPath(db, collection);
        JSONArray documents = getDocuments(collectionPath);
        int documentIndex = -1;
        for (int i=0; i < documents.size(); i++){
            JSONObject document = (JSONObject) documents.get(i);
            if (Objects.equals(document.get("_id").toString(), id)){
                documentIndex = i;
                break;
            }
        }

        if (documentIndex != -1){
            JSONObject updatedObject = (JSONObject) documents.get(documentIndex);
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                // do not allow _id edit
                if (!Objects.equals(key, "_id"))
                    updatedObject.put(key, value);
            }
            writeDocumentsToCollection(collectionPath, documents);
        }
    }

    CollectionFileController(String collectionPath){
        this.collectionPath = collectionPath;
    }

    public JSONArray getAllDocuments() throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        JSONArray documentsFromFile = (JSONArray) collection.getOrDefault("documents", new JSONArray());
        return documentsFromFile;
    }

    public JSONObject getDocumentFromFile(String id) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        JSONArray documentsFromFile = (JSONArray) collection.getOrDefault("documents", new JSONArray());
        for (int i=0; i< documentsFromFile.size(); i++){
            JSONObject o = (JSONObject) documentsFromFile.get(i);
            String _id = (String) o.get("_id");
            if (Objects.equals(_id, id)){
                return o;
            }
        }
        return null;
    }


    // indexes

    public ArrayList<String> getIndexes() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        return (ArrayList<String>) collection.getOrDefault("indexes", null);

    }

    public void createIndex(String field) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        FileWriter file = new FileWriter(collectionPath);
        JSONArray indexesFromFile = (JSONArray) collection.getOrDefault("indexes", new JSONArray());
        indexesFromFile.add(field);
        collection.put("indexes", indexesFromFile);
        file.write(collection.toJSONString());
        file.flush();
    }

    public void deleteIndex(String field) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject collection = (JSONObject) parser.parse(new FileReader(collectionPath));
        FileWriter file = new FileWriter(collectionPath);

        JSONArray indexesFromFile = (JSONArray) collection.getOrDefault("indexes", new JSONArray());
        for (int i=0; i<indexesFromFile.size(); i++){
            if (Objects.equals(indexesFromFile.get(i).toString(), field)){
                indexesFromFile.remove(i);
                break;
            }
        }
        collection.put("indexes", indexesFromFile);
        file.write(collection.toJSONString());
        file.flush();
    }

}
