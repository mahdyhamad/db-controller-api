package com.example.dbcontrollerapi;

import Database.DatabaseFileController;
import Collection.CollectionFileController;

import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


@RestController
public class WriteController {

    // Document related
    @PostMapping(value = "create-document/{db}/{collection}/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addDocument(
        @PathVariable String db, @PathVariable String collection,
        @RequestBody HashMap<String, Object> data
    ){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        try {
            CollectionFileController.addDocument(
                db, collection, data
            );
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "update-document/{db}/{collection}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDocument(
        @PathVariable String db, @PathVariable String collection, @RequestBody HashMap<String, Object> data
    ){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        String id = (String) data.getOrDefault("id", null);
        assert id != null: "Document id was not provided";
        data.remove("id");

        try {
            CollectionFileController.updateDocument(
                    db, collection, id, data
            );
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("delete-document/{db}/{collection}")
    public void deleteDocument(
        @PathVariable String db, @PathVariable String collection, @RequestBody HashMap<String, String> data
    ){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        String id = data.getOrDefault("_id", null);
        assert id != null: "Document id was not provided";

        try {
            CollectionFileController.deleteDocument(db, collection, id);
        }
        catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


    // Collection Related
    @PostMapping("create-collection/{db}/{collection}")
    public void createCollection(@PathVariable String db, @PathVariable String collection){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert !CollectionFileController.collectionExists(collection): "Collection already exist";
        try{
            CollectionFileController.createCollection(db,collection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("update-collection/{db}/{collection}")
    public void updateCollection(@PathVariable String db, @PathVariable String collection){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";
    }

    @PostMapping("delete-collection/{db}/{collection}")
    public void deleteCollection(@PathVariable String db, @PathVariable String collection){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

    }


    // Database Related
    @PostMapping("create-db/{db}")
    public void createDatabase(@PathVariable String db){
        assert !DatabaseFileController.databaseExists(db): "Database already exist";
    }

    @PostMapping("update-db/{db}")
    public void updateDatabase(@PathVariable String db, @RequestBody Map<String, String> data){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        if (data.get("name") != null){
            DatabaseFileController.renameDatabase(db, data.get("name"));
        }
    }

    @PostMapping("delete-db/{db}")
    public void deleteDatabase(@PathVariable String db){
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        DatabaseFileController.deleteDatabase(db);
    }



}
