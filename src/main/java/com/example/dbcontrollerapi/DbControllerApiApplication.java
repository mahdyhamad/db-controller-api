package com.example.dbcontrollerapi;

import Auth.AuthManager;
import Connection.ConnectionManager;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@SpringBootApplication
public class DbControllerApiApplication {
	/* the main controller that receives requests from client */

	List<String> readOnlyContainerIds;
	ConnectionManager connectionManager;
	AuthManager authManager;

	public DbControllerApiApplication(){
		connectionManager = new ConnectionManager();
		authManager = new AuthManager();
		connectionManager.killContainers("db-reado-api");
		connectionManager.createContainer();
	}
	public static void main(String[] args) {
		SpringApplication.run(DbControllerApiApplication.class, args);
	}

	@GetMapping("getConnection")
	public String getConnection(){
		return this.connectionManager.getReadConnection();
	}

	@PostMapping("scaleUp")
	public int scaleUp(){
		this.connectionManager.createContainer();
		return 1;
	}
}
