// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UriUtils {

    /**
     * Returns a map of query parameter fields to values from the URI.
     * Returns null if there are no query parameters or if the URI is invalid.
     * <p>
     * Adapted from http://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection.
     *
     * @param uri a Uniform Resource Identifier
     * @return Query parameters of the URI.
     */
    public static Map<String, String> splitQuery(URI uri) {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            URL url = uri.toURL();
            Map<String, List<String>> queryParametersMap = new LinkedHashMap<String, List<String>>();
            if (url.getQuery() == null) {
                return result;
            }
            String[] queryParameters = url.getQuery().split("&");
            for (String queryParameter : queryParameters) {
                int index = queryParameter.indexOf("=");
                String key = index > 0 ?
                        URLDecoder.decode(queryParameter.substring(0, index), "UTF-8") : queryParameter;
                if (!queryParametersMap.containsKey(key)) {
                    queryParametersMap.put(key, new LinkedList<String>());
                }
                String value = index > 0 && queryParameter.length() > index + 1 ?
                        URLDecoder.decode(queryParameter.substring(index + 1), "UTF-8") : null;
                queryParametersMap.get(key).add(value);
            }
            for (Map.Entry<String, List<String>> entry : queryParametersMap.entrySet()) {
                result.put(entry.getKey(), String.join(",", entry.getValue()));
            }
            return result;
        } catch (MalformedURLException e) {
            return result;
        } catch (UnsupportedEncodingException e) {
            return result;
        }
    }
}
