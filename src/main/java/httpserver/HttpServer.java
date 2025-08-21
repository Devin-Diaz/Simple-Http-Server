package httpserver;

import httpserver.config.Configuration;
import httpserver.config.ConfigurationManager;
import httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using port {}", config.getPort());
        LOGGER.info("Using webroot {}", config.getWebRoot());

        ServerListenerThread serverListenerThread = null;
        try {
            serverListenerThread = new ServerListenerThread(config.getPort(), config.getWebRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverListenerThread.start();
    }
}
