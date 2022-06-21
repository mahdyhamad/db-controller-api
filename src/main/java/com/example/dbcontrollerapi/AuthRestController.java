package com.example.dbcontrollerapi;

import Auth.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AuthRestController {
    @Autowired
    private ApplicationContext context;

    @PostMapping(value = "/validateJwtToken/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean validateJwtToken(@RequestBody HashMap<String, Object> data){
        String token = data.getOrDefault("token", null).toString();
        return context.getBean("getAuthController", AuthController.class).validateJwtToken(token);
    }

    @PostMapping(value = "/getToken/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getToken(@RequestBody HashMap<String, Object> data){
        System.out.println(data);
        String username = data.getOrDefault("username", "").toString();
        String password = data.getOrDefault("password", "").toString();

        String token = context.getBean("getAuthController", AuthController.class).getToken(username, password);
        System.out.println(context.getBean("getAuthController", AuthController.class).decodeJWT(token));
        return token;
    }

}
