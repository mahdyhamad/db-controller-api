package com.example.dbcontrollerapi;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class DbControllerApiApplication {
	/* the main controller that receives requests from client */

	List<String> readOnlyContainerIds;

	public DbControllerApiApplication(){
		// TODO: build and run read-only docker app with minimum of 1 node
		// on-startup, run and connect to a docker image. To sync existing data, docker
		// volumes are used.
		String baseDir = System.getProperty("user.dir");
		this.readOnlyContainerIds = new ArrayList<>();
		DockerClient dockerClient = DockerClientBuilder.getInstance().build();

		ExposedPort tcp8080 = ExposedPort.tcp(8080);
		Ports portBindings = new Ports();
		portBindings.bind(tcp8080,Ports.Binding.bindPort(6000));

		CreateContainerResponse container = dockerClient
				.createContainerCmd("db-reado-api:latest")
				.withExposedPorts(tcp8080)
				.withPortBindings(portBindings)
				.withBinds(Bind.parse(baseDir + "/db:/workspace/db-reado-api/db"))
				.exec();
		dockerClient.startContainerCmd(container.getId()).exec();
	}
	public static void main(String[] args) {
		SpringApplication.run(DbControllerApiApplication.class, args);
	}

}
