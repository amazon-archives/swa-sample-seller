// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Pretty prints a JSON string.
     *
     * @param json A String of valid JSON.
     * @return Pretty printed JSON, or the original json if it can't be pretty printed.
     */
    public static String prettyPrintJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object object = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return json;
        } catch (IOException e) {
            return json;
        }
    }
}
