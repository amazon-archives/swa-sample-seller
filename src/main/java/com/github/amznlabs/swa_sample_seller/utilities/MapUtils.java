// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import javax.ws.rs.core.MultivaluedMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    /**
     * Converts a MultivaluedMap<String, String> to a Map<String, String>.
     * .
     *
     * For example, a key-value pair in multivaluedMap of: "hello", ("what", "is", "up")
     * will be converted to the following key-value in map: "hello", "what,is,up"
     */
    public static Map<String, String> multivaluedMap2Map(MultivaluedMap<String, String> multivaluedMap) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : multivaluedMap.entrySet()) {
            String valuesList = String.join(",", entry.getValue());
            map.put(entry.getKey(), valuesList);
        }
        return map;
    }
}
