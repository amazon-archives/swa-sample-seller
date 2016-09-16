// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class UriUtilsTest {
    private final SoftAssert softAssert = new SoftAssert();

    @Test
    public void validSplitQuery() {
        URI uri = null;
        try {
            uri = new URI("https://google.com.ua/oauth/authorize?" +
                    "client_id=SS&response_type=code&scope=N_FULL&" +
                    "access_type=offline&redirect_uri=http://localhost/Callback");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Tried to create URI object from invalid URI string", e);
        }

        Map<String, String> queryParameters = UriUtils.splitQuery(uri);

        String error = "query parameter value did not match";
        softAssert.assertEquals("SS", queryParameters.get("client_id"), error);
        softAssert.assertEquals("code", queryParameters.get("response_type"), error);
        softAssert.assertEquals("offline", queryParameters.get("access_type"), error);
        softAssert.assertEquals("http://localhost/Callback", queryParameters.get("redirect_uri"), error);
        softAssert.assertAll();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void nullSplitQuery() throws URISyntaxException {
        UriUtils.splitQuery(null);
    }
}
