// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.amznlabs.swa_sample_seller.models.SellerAccessTokenRequestJson;
import com.github.amznlabs.swa_sample_seller.utilities.StringUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class RequestResponseWrapperTest {
    private final SoftAssert softAssert = new SoftAssert();
    private static final String EXPECTED_RESPONSE_BODY =
            "{\"error_description\":\"Client authentication failed\",\"error\":\"invalid_client\"}";
    private static final String EXPECTED_REQUEST_BODY =
            "{\"grant_type\":\"client_credentials\",\"scope\":" +
                    "\"swa:subscriptions\",\"client_id\":\"" + Constants.INVALID_SELLER_CLIENT_ID +
                    "\",\"client_secret\":\"" + Constants.INVALID_SELLER_CLIENT_SECRET + "\"}";

    @Test
    public void validRequestAndResponseWrappers() {
        // Prepare a request body.
        SellerAccessTokenRequestJson sellerAccessTokenRequestJson = new SellerAccessTokenRequestJson(
                Constants.INVALID_SELLER_CLIENT_ID, Constants.INVALID_SELLER_CLIENT_SECRET);
        String sellerAccessTokenRequestString = null;
        try {
            sellerAccessTokenRequestString = new ObjectMapper().writeValueAsString(sellerAccessTokenRequestJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize Java object to JSON", e);
        }

        // Send a POST to SWA Subscriptions API.
        RequestWrapper requestWrapper = new RequestWrapper(sellerAccessTokenRequestString);
        Client client = ClientBuilder
                .newClient()
                .register(requestWrapper);
        Response response = client
                .target("https://api.amazon.com/auth/O2/token")
                .request()
                .accept("application/json")
                .post(Entity.json(sellerAccessTokenRequestString));

        // Did the wrapper save the request properly?
        RequestResponseWrapper requestResponseWrapper =
                new RequestResponseWrapper(requestWrapper, new ResponseWrapper(response));
        softAssert.assertEquals(requestWrapper, requestResponseWrapper.getRequestWrapper(),
                "requestResponseWrapper did not save requestWrapper");

        // Did the wrapper save the response properly?
        ResponseWrapper responseWrapper = requestResponseWrapper.getResponseWrapper();
        softAssert.assertNotNull(requestResponseWrapper.getResponseWrapper(), "got null for .getResponseWrapper");

        // Did we catch the request body?
        softAssert.assertEquals(StringUtils.sort(EXPECTED_REQUEST_BODY), StringUtils.sort(requestWrapper.getBody()),
                "did not got expected request body");

        // Did we save the response body?
        softAssert.assertEquals(StringUtils.sort(EXPECTED_RESPONSE_BODY), StringUtils.sort(responseWrapper.getBody()),
                "did not got expected response body");

        softAssert.assertAll();
    }
}
