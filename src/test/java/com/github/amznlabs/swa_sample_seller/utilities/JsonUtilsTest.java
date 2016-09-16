// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonUtilsTest {
    private static final String VALID_JSON =
            "{\"pushNotificationVersion\":2,\"operation\":\"asdflkjasf\",\"subscription\":" +
            "{\"beneficiaryIds\":null,\"beneficiaries\":[{\"type\":\"asfdaf\",\"id\":\"asdlj;kasdfl;j\"}]," +
            "\"receiptId\":\"a;lsfdjasd;lj\",\"productId\":\"asdkjffsd;lj\"}}";
    private static final String INVALID_JSON =
            "{pushNotificationVersion\":2,operation:\"asdflkjasf\",\"subscription\":" +
            "{\"beneficiaryIds\":null,,,\"beneficiaries\":[{\"type\":\"asfdaf\",\"id\":\"asdlj;kasdfl;j\"}]," +
            "\"receiptId\":\"a;lsfdjasd;lj\",\"productId\":\"asdkjffsd;lj\"}}";

    @Test
    public void validPrettyPrintJson() {
        String expected = "{\n" +
                "  \"pushNotificationVersion\" : 2,\n" +
                "  \"operation\" : \"asdflkjasf\",\n" +
                "  \"subscription\" : {\n" +
                "    \"beneficiaryIds\" : null,\n" +
                "    \"beneficiaries\" : [ {\n" +
                "      \"type\" : \"asfdaf\",\n" +
                "      \"id\" : \"asdlj;kasdfl;j\"\n" +
                "    } ],\n" +
                "    \"receiptId\" : \"a;lsfdjasd;lj\",\n" +
                "    \"productId\" : \"asdkjffsd;lj\"\n" +
                "  }\n" +
                "}";
        String actual = JsonUtils.prettyPrintJson(VALID_JSON).replaceAll("\\r\\n", "\n");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void invalidPrettyPrintJson() {
        String actual = JsonUtils.prettyPrintJson(INVALID_JSON);
        Assert.assertEquals(INVALID_JSON, actual);
    }
}
