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
     * Adapted from http://stackoverflow.com/questions/4021851/join-string-list-elements-with-a-delimiter-in-one-step.
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
                String key = index > 0 ? URLDecoder.decode(queryParameter.substring(0, index), "UTF-8") : queryParameter;
                if (!queryParametersMap.containsKey(key)) {
                    queryParametersMap.put(key, new LinkedList<String>());
                }
                String value = index > 0 && queryParameter.length() > index + 1 ? URLDecoder.decode(queryParameter.substring(index + 1), "UTF-8") : null;
                queryParametersMap.get(key).add(value);
            }
            for (String key : queryParametersMap.keySet()) {
                result.put(key, String.join(",", queryParametersMap.get(key)));
            }
            return result;
        } catch (MalformedURLException e) {
            return result;
        } catch (UnsupportedEncodingException e) {
            return result;
        }
    }
}
