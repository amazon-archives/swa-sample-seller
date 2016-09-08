package com.github.amznlabs.swa_sample_seller.utilities;

import javax.ws.rs.core.MultivaluedMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtils {

    /**
     * Converts a MultivaluedMap<String, String> to a Map<String, String>.
     *
     * @param multivaluedMap
     * @return
     */
    public static Map<String, String> multivaluedMap2Map(MultivaluedMap<String, String> multivaluedMap) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String key : multivaluedMap.keySet()) {
            String valuesList = String.join(",", multivaluedMap.get(key));
            map.put(key, String.format("%s: %s\n", key, valuesList));
        }
        return map;
    }
}
