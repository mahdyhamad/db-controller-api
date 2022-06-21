package com.example.dbcontrollerapi;

import Auth.AuthController;
import Auth.AuthRequestManager;
import Config.Configuration;
import Controller.DBController;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class ReadDataController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/{db}/{collection}")
    public ArrayList<JSONObject> getByKey(
            @PathVariable String db,
            @PathVariable String collection,
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "value", required = false) String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String resourcePath = Configuration.DATA_PATH + db + "/" + collection + ".json";
        // Authentication
        boolean isAuthenticated = AuthRequestManager.isAuthorized(
            context.getBean("getAuthController", AuthController.class),
            request, resourcePath
        );

        if (!isAuthenticated){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        System.out.println("GET " + db + " -> " + collection + " -> " + field + ": " + value);
        return context.getBean("getDBController", DBController.class)
                        .getDatabase(db)
                        .getCollection(collection)
                        .find(field, value);
    }
}
