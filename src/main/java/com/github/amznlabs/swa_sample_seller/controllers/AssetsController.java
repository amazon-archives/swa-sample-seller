// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers;

import com.github.amznlabs.swa_sample_seller.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Serves static content.
 */
@Path("/")
public class AssetsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsController.class);

    /**
     * Ex: https://127.0.0.1/assets/js/login.js will retrieve the file assets/js/login.js.
     */
    @GET
    @Path("/assets/{folder}/{filename}")
    public Response assets(@PathParam("folder") String folder, @PathParam("filename") String filename) {
        try {
            String path = Config.getInstance().getResourcePath(String.format("/assets/%s/%s", folder, filename));
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return Response.ok(bytes).type(URLConnection.guessContentTypeFromName(filename)).build();
        } catch (IOException e) {
            LOGGER.error("Could not find asset: ", e);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
