package com.github.amznlabs.swa_sample_seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final Config INSTANCE = new Config();
    private static final String handlebarsFileExtension = ".hbs";
    private static final String handlebarsDirectoryPath = "/";
    private final String resourcePath = "src/main/resources";

    private String clientId;
    private String redirectUri;
    private int port;

    private Handlebars handlebars;

    /**
     * config.json parses into this class.
     */
    private static class JsonConfig {
        public String clientId;
        public String redirectUri;
        public int port;
    }

    /**
     * Private constructor to prevent other code from instantiating this.
     */
    private Config() {
        if (!Files.exists(Paths.get(resourcePath))) {
            logger.error("Could not find path to resources. Run JAR file from repo root.");
            System.exit(1);
        }
        try {
            JsonConfig jsonConfig;
            jsonConfig = new ObjectMapper().readValue(new File(getResourcePath("/config.json")), JsonConfig.class);
            this.clientId = jsonConfig.clientId;
            this.redirectUri = jsonConfig.redirectUri;
            this.port = jsonConfig.port;
        } catch (IOException e) {
            logger.error("Failed to read config.json: {}", e);
        }
        TemplateLoader loader = new FileTemplateLoader(getResourcePath(handlebarsDirectoryPath), handlebarsFileExtension);
        this.handlebars = new Handlebars(loader);
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

    public String getRedirectUri() {
        return redirectUri;
    }

    public int getPort() {
        return port;
    }

    public Handlebars getHandlebars() {
        return handlebars;
    }
}
