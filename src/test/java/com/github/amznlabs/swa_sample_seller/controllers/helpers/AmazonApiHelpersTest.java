// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

public class AmazonApiHelpersTest {
    private static final String CUSTOMER_PROFILE_ERROR = "invalid_token";
    private static final String SELLER_ACCESS_TOKEN_ERROR = "invalid_client";
    private static final String SUBSCRIPTIONS_ERROR = "Request is not authorized";

    @Test
    public void badCustomerProfileRequest() {
        RequestResponseWrapper requestResponseWrapper =
                AmazonApiHelpers.requestCustomerProfile(Constants.INVALID_CUSTOMER_ACCESS_TOKEN);
        ResponseWrapper responseWrapper = requestResponseWrapper.getResponseWrapper();
        Assert.assertTrue(responseWrapper.getBody().contains(CUSTOMER_PROFILE_ERROR),
                "body did not include expected error");
        Assert.assertEquals(Response.Status.BAD_REQUEST, responseWrapper.getRawResponse().getStatusInfo());
    }

    @Test
    public void badSellerAccessTokenRequest() {
        RequestResponseWrapper requestResponseWrapper =
                AmazonApiHelpers.requestSellerAccessToken(
                        Constants.INVALID_SELLER_CLIENT_ID, Constants.INVALID_SELLER_CLIENT_SECRET);
        ResponseWrapper responseWrapper = requestResponseWrapper.getResponseWrapper();
        Assert.assertTrue(responseWrapper.getBody().contains(SELLER_ACCESS_TOKEN_ERROR));
        Assert.assertEquals(Response.Status.UNAUTHORIZED, responseWrapper.getRawResponse().getStatusInfo());
    }

    @Test
    public void badSubscriptionsRequest() {
        RequestResponseWrapper requestResponseWrapper =
                AmazonApiHelpers.requestSubscriptions(
                        Constants.INVALID_CUSTOMER_USER_ID, Constants.INVALID_SELLER_ACCESS_TOKEN);
        ResponseWrapper responseWrapper = requestResponseWrapper.getResponseWrapper();
        Assert.assertTrue(responseWrapper.getBody().contains(SUBSCRIPTIONS_ERROR));
        Assert.assertEquals(Response.Status.FORBIDDEN, responseWrapper.getRawResponse().getStatusInfo());
    }
}
