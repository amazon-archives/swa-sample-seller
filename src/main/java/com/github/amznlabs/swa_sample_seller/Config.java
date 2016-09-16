// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import lombok.Data;
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
public final class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static final Config INSTANCE = new Config();
    private static final String HANDLEBARS_FILE_EXTENSION = ".hbs";
    private static final String HANDLEBARS_DIRECTORY_PATH = "/views";
    private static final String RESOURCE_PATH = "src/main/resources";

    private JsonConfig jsonConfig;
    private Handlebars handlebars;

    /**
     * config.json parses into this class.
     */
    @Data
    private static class JsonConfig {
        private String clientId;
        private String clientSecret;
        private String detailPageUrl;
        private String redirectUri;
        private String logoutRedirectUrl;
        private int port;
        private String awsResourcesName;
        private String awsAccessKeyId;
        private String awsSecretAccessKey;
        private String awsRegionName;
    }

    /**
     * Private constructor to prevent other code from instantiating this.
     */
    private Config() {
        if (!Files.exists(Paths.get(RESOURCE_PATH))) {
            throw new RuntimeException("Could not find path to resources. Run JAR file from repo root.");
        }
        try {
            jsonConfig = new ObjectMapper().readValue(new File(getResourcePath("/config.json")), JsonConfig.class);
        } catch (IOException e) {
            LOGGER.error("Failed to read config.json: {}", e);
        }
        TemplateLoader loader = new FileTemplateLoader(getResourcePath(HANDLEBARS_DIRECTORY_PATH),
                HANDLEBARS_FILE_EXTENSION);
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
     * For example, if "apiDumpViewModel.html" was a file in the resources folder,
     * then this application should call config.getResourcePath("/apiDumpViewModellanding.html")
     * to access it.
     *
     * @param path Absolute path.
     * @return Path that this application can use.
     */
    public String getResourcePath(String path) {
        return RESOURCE_PATH + path;
    }

    public String getClientId() {
        return jsonConfig.clientId;
    }

    public String getClientSecret() {
        return jsonConfig.clientSecret;
    }

    public String getDetailPageUrl() {
        return jsonConfig.detailPageUrl;
    }

    public String getRedirectUri() {
        return jsonConfig.redirectUri;
    }

    public String getLogoutRedirectUrl() {
        return jsonConfig.logoutRedirectUrl;
    }

    public int getPort() {
        return jsonConfig.port;
    }

    public Handlebars getHandlebars() {
        return handlebars;
    }
}
