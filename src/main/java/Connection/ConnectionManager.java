package Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.command.CreateContainerResponse;

public class ConnectionManager {

    String baseDirectory;
    DockerClient dockerClient;
    List<String> containerIds;
    List<Connection> connections;


    public ConnectionManager(){
        this.containerIds = new ArrayList<>();
        this.baseDirectory = System.getProperty("user.dir");
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }

    public void createContainer(){
        ExposedPort tcp8080 = ExposedPort.tcp(8080);
        Ports portBindings = new Ports();
        portBindings.bind(tcp8080,Ports.Binding.bindPort(getRandomPortNumber()));

        CreateContainerResponse container = dockerClient
                .createContainerCmd("db-reado-api:latest")
                .withExposedPorts(tcp8080)
                .withPortBindings(portBindings)
                .withBinds(Bind.parse(baseDirectory + "/db:/workspace/db-reado-api/db"))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        containerIds.add(container.getId());
        getContainer(container.getId());
    }

    public int getRandomPortNumber(){
        int MIN_PORT = 1000;
        int MAX_PORT = 6000;
        return (int)(Math.random()*(MAX_PORT - MIN_PORT + 1) + MIN_PORT);
    }

    public Container getContainer(String containerId){
        List<Container> containers = this.dockerClient.listContainersCmd()
                .withShowAll(true)
                .withIdFilter(Collections.singleton(containerId)).exec();
        if (containers.size() > 0)
            return containers.get(0);
        return null;
    }

    public void killContainers(String image){
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowAll(true)
                .withAncestorFilter(Collections.singleton(image))
                .exec();
        for (Container container: containers){
            System.out.println(container.getId());
            dockerClient
                    .removeContainerCmd(container.getId())
                    .withForce(true)
                    .withContainerId(container.getId())
                    .exec();
        }

    }

    public String getReadConnection(){
        // TODO: implement load-balancing algorithm
        int index = (int) (Math.random()*(containerIds.size()) - 1);
        int port = getContainer(containerIds.get(index)).getPorts()[0].getPublicPort();
        return "http://localhost:" + port + "/";
    }
}
