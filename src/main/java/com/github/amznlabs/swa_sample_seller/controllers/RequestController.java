// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.amznlabs.swa_sample_seller.Config;
import com.github.amznlabs.swa_sample_seller.controllers.helpers.AmazonApiHelpers;
import com.github.amznlabs.swa_sample_seller.controllers.helpers.RequestResponseWrapper;
import com.github.amznlabs.swa_sample_seller.controllers.helpers.TemplateRenderer;
import com.github.amznlabs.swa_sample_seller.models.CustomerProfileResponseJson;
import com.github.amznlabs.swa_sample_seller.models.SellerAccessTokenResponseJson;
import com.github.amznlabs.swa_sample_seller.models.SubscriptionsResponseJson;
import com.github.amznlabs.swa_sample_seller.viewmodels.ApiDumpViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.Optional;

/**
 * Routes user requests to response handlers.
 */
@lombok.Data
@Path("/")
public class RequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    /**
     * Landing page.
     *
     * @return HTTP response
     * @throws IOException
     */
    @GET
    @Path("/")
    public String index() throws IOException {
        return TemplateRenderer
                .compile("landing")
                .apply(TemplateRenderer
                        .newContextBuilder()
                        .combine("config", Config.getInstance())
                        .build());
    }

    /**
     * API dump page.
     *
     * @param customerAccessToken LWA will set this access_token query parameter.
     * @return HTTP response
     * @throws IOException
     */
    @GET
    @Path("/login")
    public String login(@QueryParam("access_token") String customerAccessToken) throws IOException {
        ApiDumpViewModel apiDumpViewModel = new ApiDumpViewModel();
        boolean apiCallsSucceeded = false;
        boolean hasSubscription = false;
        String name = "NAME NOT FOUND";

        // customer access token -> customer profile.
        Optional<CustomerProfileResponseJson> customerProfileResponseJson =
                getCustomerProfile(customerAccessToken, apiDumpViewModel);

        if (customerProfileResponseJson.isPresent()) {
            name = customerProfileResponseJson.get().getName();

            // Use seller's clientId and clientSecret to retrieve seller's access_token.
            Optional<SellerAccessTokenResponseJson> sellerAccessTokenResponseJson =
                    getSellerAccessToken(
                            Config.getInstance().getClientId(),
                            Config.getInstance().getClientSecret(),
                            apiDumpViewModel);

            if (sellerAccessTokenResponseJson.isPresent()) {
                // Use customer user_id + seller's access_token to retrieve customer subscriptions information.
                Optional<SubscriptionsResponseJson> subscriptionsResponseJson =
                        getSubscriptions(
                                customerProfileResponseJson.get().getUserId(),
                                sellerAccessTokenResponseJson.get().getAccessToken(),
                                apiDumpViewModel);

                if (subscriptionsResponseJson.isPresent()) {
                    // Check if this customer has a subscription.
                    hasSubscription = subscriptionsResponseJson.get().getSubscriptions().size() > 0;

                    // All API calls have succeeded.
                    apiCallsSucceeded = true;
                }
            }
        }

        return TemplateRenderer
                .compile("subscriptions").apply(TemplateRenderer
                        .newContextBuilder()
                        .combine("config", Config.getInstance())
                        .combine("apiCallsSucceeded", apiCallsSucceeded)
                        .combine("hasSubscription", hasSubscription)
                        .combine("apiDump", apiDumpViewModel)
                        .combine("name", name)
                        .build());
    }

    /**
     * Retrieve customer profile with customer access_token.
     * Logs request and response to console and updates viewModel.
     *
     * @return CustomerProfileResponseJson, or null if the request failed.
     */
    private static Optional<CustomerProfileResponseJson> getCustomerProfile(String customerAccessToken,
                                                                            ApiDumpViewModel apiDumpViewModel) {
        ObjectMapper mapper = new ObjectMapper();

        // Retrieve customer profile.
        RequestResponseWrapper requestResponseWrapper = AmazonApiHelpers.requestCustomerProfile(customerAccessToken);

        // Bind request/responseWrapper info to logs and viewModel.
        apiDumpViewModel.setCustomerProfileRequest(requestResponseWrapper.getRequestWrapper().getRequestViewModel());
        apiDumpViewModel.setCustomerProfileResponse(requestResponseWrapper.getResponseWrapper().getResponseViewModel());
        LOGGER.info("\n{}\n{}", requestResponseWrapper.getRequestWrapper().dump(),
                requestResponseWrapper.getResponseWrapper().dump());

        // Check response status.
        apiDumpViewModel.setCustomerProfileRequestSucceeded(requestResponseWrapper.getResponseWrapper().ok());

        if (apiDumpViewModel.getCustomerProfileRequestSucceeded()) {
            // Parse response.
            try {
                return Optional.of(mapper.readValue(
                        requestResponseWrapper.getResponseWrapper().getBody(), CustomerProfileResponseJson.class));
            } catch (IOException e) {
                LOGGER.error("Failed to parse customer profile response body to JSON.", e);
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieve seller access_token with seller clientId and clientSecret.
     * Logs request and response to console and updates viewModel.
     *
     * @return SellerAccessTokenResponseJson, or null if the API request failed.
     */
    private static Optional<SellerAccessTokenResponseJson> getSellerAccessToken(String clientId, String clientSecret,
                                                                                ApiDumpViewModel apiDumpViewModel) {
        ObjectMapper mapper = new ObjectMapper();

        // Retrieve seller token.
        RequestResponseWrapper requestResponseWrapper = AmazonApiHelpers.requestSellerAccessToken(
                clientId, clientSecret);

        // Bind request/responseWrapper info to logs and viewModel.
        apiDumpViewModel.setSellerAccessTokenRequest(requestResponseWrapper.getRequestWrapper().getRequestViewModel());
        apiDumpViewModel.setSellerAccessTokenResponse(
                requestResponseWrapper.getResponseWrapper().getResponseViewModel());
        LOGGER.info("\n{}\n{}", requestResponseWrapper.getRequestWrapper().dump(),
                requestResponseWrapper.getResponseWrapper().dump());

        // Check response status.
        apiDumpViewModel.setSellerAccessTokenRequestSucceeded(requestResponseWrapper.getResponseWrapper().ok());

        if (apiDumpViewModel.getSellerAccessTokenRequestSucceeded()) {
            // Parse response.
            try {
                return Optional.of(mapper.readValue(
                        requestResponseWrapper.getResponseWrapper().getBody(), SellerAccessTokenResponseJson.class));
            } catch (IOException e) {
                LOGGER.error("Failed to parse seller token response body to JSON.", e);
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieve customer subscriptions information
     */
    private static Optional<SubscriptionsResponseJson> getSubscriptions(String userId, String accessToken,
                                                                        ApiDumpViewModel apiDumpViewModel) {
        // Retrieve subscriptions information.
        RequestResponseWrapper requestResponseWrapper = AmazonApiHelpers.requestSubscriptions(userId, accessToken);

        // Bind request/responseWrapper info to logs and viewModel.
        apiDumpViewModel.setSubscriptionsRequest(requestResponseWrapper.getRequestWrapper().getRequestViewModel());
        apiDumpViewModel.setSubscriptionsResponse(requestResponseWrapper.getResponseWrapper().getResponseViewModel());
        LOGGER.info("\n{}\n{}", requestResponseWrapper.getRequestWrapper().dump(),
                requestResponseWrapper.getResponseWrapper().dump());

        // Check response status.
        // HANDLE SWA API RESPONSE CODE
        apiDumpViewModel.setSubscriptionsRequestSucceeded(requestResponseWrapper.getResponseWrapper().ok());

        if (apiDumpViewModel.getSubscriptionsRequestSucceeded()) {
            // Parse response.
            try {
                return Optional.of(parseSubscriptionsResponse(requestResponseWrapper.getResponseWrapper().getBody()));
            } catch (IOException e) {
                LOGGER.error("Failed to parse subscriptions response body to JSON.", e);
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parse the body of a response from the List Subscriptions API into JSON.
     */
    private static SubscriptionsResponseJson parseSubscriptionsResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, SubscriptionsResponseJson.class);
    }
}
