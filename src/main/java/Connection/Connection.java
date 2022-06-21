package Connection;

import com.github.dockerjava.api.model.Container;

public class Connection {
    String url;
    int port;
    Container container;

    Connection(String url, int port, Container container){
        this.url = url;
        this.port = port;
        this.container = container;
    }
}
