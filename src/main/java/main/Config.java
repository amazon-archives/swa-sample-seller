package main;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Singleton class to store configuration parameters.
 * <p>
 * All code should access config parameters through this variable.
 * See http://stackoverflow.com/a/96436 for some justification.
 */
public class Config {
    private static final Config INSTANCE = new Config();

    private final String resourcePath = "src/main/resources";
    private String clientId;
    private String host;
    private int port;
    private String loginPath;

    /**
     * config.json parses into this class.
     */
    private static class JsonConfig {
        public String clientId;
        public String host;
        public int port;
        public String loginPath;
    }

    /**
     * Private constructor to prevent other code from instantiating this.
     */
    private Config() {
        if (!Files.exists(Paths.get(resourcePath))) {
            System.err.println("Could not find path to resources.");
            System.err.println("Run JAR file from swa-sample-seller directory.");
            System.exit(1);
        }
        try {
            JsonConfig jsonConfig;
            jsonConfig = new ObjectMapper().readValue(new File(getResourcePath("/config.json")), JsonConfig.class);
            this.clientId = jsonConfig.clientId;
            this.host = jsonConfig.host;
            this.port = jsonConfig.port;
            this.loginPath = jsonConfig.loginPath;
        } catch (IOException e) {
            System.err.println("failed to read config.json");
            e.printStackTrace();
        }
    }

    /**
     * @return Singleton instance of Config.
     */
    public static Config getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the correct path of this resource.
     * <p>
     * For example, if "view/landing.html" was a file in the resources folder,
     * then this application should call config.getResourcePath("/view/landing.html")
     * to access it.
     *
     * @param path Absolute path.
     * @return Path that this application can use.
     */
    public String getResourcePath(String path) {
        return resourcePath + path;
    }

    public String getClientId() {
        return clientId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getLoginPath() {
        return loginPath;
    }
}

