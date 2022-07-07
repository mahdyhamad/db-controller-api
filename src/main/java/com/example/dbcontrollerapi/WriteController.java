package com.example.dbcontrollerapi;

import Utils.OnResourceUpdate;
import Connection.ConnectionManager;
import Database.DatabaseFileController;
import Collection.CollectionFileController;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;



@RestController
public class WriteController {

    @Autowired
    private ApplicationContext context;

    // Document related
    @PostMapping(value = "add-document/{db}/{collection}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addDocument(
        @PathVariable String db, @PathVariable String collection,
        @RequestBody HashMap<String, Object> data
    ){
        // TODO: auth
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
        if (id == null){
            throw new RuntimeException("Document id was not provided");
        }
        data.remove("id");

        try {
            CollectionFileController.updateDocument(
                    db, collection, id, data
            );
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        ConnectionManager connectionManager = context.getBean("getConnectionManager", ConnectionManager.class);
        try{
            // TODO: get resource path
            OnResourceUpdate.notifyReadOnlyReplicas("test", connectionManager.getConnectionsUrls());
            System.out.println("notified");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @PostMapping("delete-document/{db}/{collection}")
    public void deleteDocument(
        @PathVariable String db, @PathVariable String collection, @RequestBody HashMap<String, String> data
    ){
        // TODO: add auth

        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        String id = data.getOrDefault("id", null);
        assert id != null: "Document id was not provided";

        try {
            CollectionFileController.deleteDocument(db, collection, id);
            // TODO: notify RORs
        }
        catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


    // Collection Related
    @PostMapping("create-collection/{db}/{collection}")
    public void createCollection(@PathVariable String db, @PathVariable String collection){

        // TODO: add auth

        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert !CollectionFileController.collectionExists(collection): "Collection already exist";
        try{
            CollectionFileController.createCollection(db,collection);
            // TODO: add new resource to acl
            // TODO: notify RORs
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "update-collection/{db}/{collection}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCollection(
        @PathVariable String db, @PathVariable String collection, @RequestBody HashMap<String, Object> data
    ){

        // TODO: add auth

        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        // update index data
        // update collection name
        String updatedName = (String) data.getOrDefault("name", null);
        if (updatedName != null){
            try {
                CollectionFileController.renameCollection(db, collection, updatedName);
                // TODO: notify RORs
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping("delete-collection/{db}/{collection}")
    public void deleteCollection(@PathVariable String db, @PathVariable String collection){
        // TODO: add auth

        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        assert CollectionFileController.collectionExists(collection): "Collection does not exist";

        try{
            CollectionFileController.deleteCollection(db, collection);
            // TODO: notify RORs
            // TODO: remove resource from ACL
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    // Database Related
    @PostMapping("create-db/{db}")
    public void createDatabase(@PathVariable String db){
        assert !DatabaseFileController.databaseExists(db): "Database already exist";
        try{
            DatabaseFileController.createDatabase(db);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "update-db/{db}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDatabase(@PathVariable String db, @RequestBody Map<String, String> data){

        // TODO: add auth

        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        String updatedName = data.getOrDefault("name", null);
        if (updatedName != null){
            try{
                DatabaseFileController.renameDatabase(db, updatedName);
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping("delete-db/{db}")
    public void deleteDatabase(@PathVariable String db){

        // TODO: add auth

        // do not allow auth database deletion
        if (Objects.equals(db, "auth")){
            return;
        }
        assert DatabaseFileController.databaseExists(db): "Database does not exist";
        try {
            DatabaseFileController.deleteDatabase(db);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
