// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.Map;

public class MapUtilsTest {
    @Test
    public void validMultivaluedMap2Map() {
        MultivaluedMap<String, String> multivaluedMap = new MultivaluedHashMap<>();
        multivaluedMap.put("hello", Arrays.asList("what", "is", "up"));
        multivaluedMap.put("lorem", Arrays.asList("ipsum", "lorem"));

        Map<String, String> map = MapUtils.multivaluedMap2Map(multivaluedMap);
        Assert.assertEquals(map.get("hello"), "what,is,up");
        Assert.assertEquals(map.get("lorem"), "ipsum,lorem");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void nullMultivaluedMap2Map() {
        MapUtils.multivaluedMap2Map(null);
    }
}
