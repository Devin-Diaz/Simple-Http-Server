package httpserver.config;

/**
 * Model class for server configuration.
 */
public class Configuration {
    private int port;
    private String webRoot;

    public int getPort() {
        return port;
    }
    public String getWebRoot() {
        return webRoot;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }
    public String toString() {
        return "Port: " + port + "\n" + "Web Root: " + webRoot;
    }
}
