package com.example.dbcontrollerapi;

import Auth.AuthController;
import Auth.AuthManager;
import Connection.ConnectionManager;
import Controller.DBController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Objects;

@RestController
@SpringBootApplication
public class DbControllerApiApplication {
	/* the main controller that receives requests from client */

	ConnectionManager connectionManager;
	AuthManager authManager;
	DBController dbController;
	AuthController authController;


	public DbControllerApiApplication(){
		connectionManager = new ConnectionManager();
		authManager = new AuthManager();
		dbController = new DBController();
		authController = new AuthController();
		try {
			connectionManager.killContainers("db-reado-api");
			connectionManager.createConnection();
		} catch (Exception e){}
	}
	@Bean
	DBController getDBController(){
		return this.dbController;
	}

	@Bean
	AuthController getAuthController(){
		return this.authController;
	}

	@Bean
	ConnectionManager getConnectionManager(){
		return this.connectionManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(DbControllerApiApplication.class, args);
	}

	@GetMapping("getReadConnection")
	public String getReadConnection(){
		// TODO: add auth
		return this.connectionManager.getReadConnection();
	}

	@PostMapping("scaleUp")
	public int scaleUp(){
		// TODO: add auth
		this.connectionManager.createConnection();
		return 1;
	}

	@PostMapping("reload-resource")
	public int onResourceUpdate(){
		// TODO: add auth
		return this.dbController.loadDatabase()? 1: 0;
	}
}
