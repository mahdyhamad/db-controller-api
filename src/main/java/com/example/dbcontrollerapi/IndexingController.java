package com.example.dbcontrollerapi;
import Collection.Collection;
import Connection.ConnectionManager;
import Controller.DBController;
import Database.Database;
import Utils.OnResourceUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class IndexingController {

    @Autowired
    private ApplicationContext context;


    @PostMapping(value = "create-index/{db}/{collection}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createIndex(
        @PathVariable String db,
        @PathVariable String collection,
        @RequestBody HashMap<String, Object> data
    ){
        // update collection json file
        String fieldToBeIndexed = data.getOrDefault("field", null).toString();
        boolean indexCreated = false;
        if (fieldToBeIndexed != null){
            DBController dbController = context.getBean("getDBController", DBController.class);
            ConnectionManager connectionManager = context.getBean("getConnectionManager", ConnectionManager.class);
            Database database = dbController.getDatabase(db);
            Collection dbCollection = database.getCollection(collection);

            indexCreated = dbCollection.createIndex(fieldToBeIndexed);

            // notify RORs
            try {
                OnResourceUpdate.notifyReadOnlyReplicas("", connectionManager.getConnectionsUrls());
            }
            catch (Exception e){

            }
        }
        return indexCreated;
    }

    @PostMapping(value = "delete-index/{db}/{collection}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteIndex(
        @PathVariable String db,
        @PathVariable String collection,
        @RequestBody HashMap<String, Object> data
    ){
        // update collection json file
        String indexFieldTobeRemoved = data.getOrDefault("field", null).toString();
        boolean indexRemoved = false;
        if (indexFieldTobeRemoved != null){
            DBController dbController = context.getBean("getDBController", DBController.class);
            ConnectionManager connectionManager = context.getBean("getConnectionManager", ConnectionManager.class);
            Database database = dbController.getDatabase(db);
            Collection dbCollection = database.getCollection(collection);
            indexRemoved = dbCollection.deleteIndex(indexFieldTobeRemoved);
            try {
                OnResourceUpdate.notifyReadOnlyReplicas("", connectionManager.getConnectionsUrls());
            }
            catch (Exception e){
                return false;
            }
            // notify RORs
        }
        return indexRemoved;
    }

}
