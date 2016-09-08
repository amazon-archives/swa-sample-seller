package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.amznlabs.swa_sample_seller.SampleSellerRuntimeException;
import com.github.amznlabs.swa_sample_seller.models.SellerAccessTokenRequestJson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * Contains methods to call Amazon APIs.
 */
public class AmazonApiHelpers {

    /**
     * Retrieve a customer profile with the customer's LWA access token.
     * <p>
     * See "Using Access Tokens to Read a Customer Profile" in
     * https://images-na.ssl-images-amazon.com/images/G/01/lwa/dev/docs/website-developer-guide._TTH_.pdf.
     *
     * @param accessToken The access token returned by LWA. See https://login.amazon.com/glossary#access_token.
     * @return ResponseViewModel containing the customer's profile. See https://developer.amazon.com/public/apis/engage/login-with-amazon/docs/customer_profile.html
     */
    public static RequestResponseWrapper requestCustomerProfile(String accessToken) {
        RequestWrapper requestWrapper = new RequestWrapper("");
        Client client = ClientBuilder
                .newClient()
                .register(requestWrapper);

        // REQUEST CUSTOMER PROFILE
        Response response = client
                .target("https://api.amazon.com/user/profile")
                .request()
                .header("x-amz-access-token", accessToken)
                .accept("application/json")
                .get();
        // REQUEST CUSTOMER PROFILE END

        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        return new RequestResponseWrapper(requestWrapper, responseWrapper);
    }

    /**
     * Retrieve an API access token with your clientId and clientSecret.
     * <p>
     * See the Subscribe with Amazon Getting Started Guide, 4.2.1.
     *
     * @param clientId     The clientId associated with your SWA subscription.
     * @param clientSecret The clientSecret associated with your SWA subscription.
     * @return ResponseViewModel containing the access token.
     */
    public static RequestResponseWrapper requestSellerAccessToken(String clientId, String clientSecret) {
        SellerAccessTokenRequestJson sellerAccessTokenRequestJson = new SellerAccessTokenRequestJson(clientId, clientSecret);
        String sellerAccessTokenRequestString = null;
        try {
            sellerAccessTokenRequestString = new ObjectMapper().writeValueAsString(sellerAccessTokenRequestJson);
        } catch (JsonProcessingException e) {
            throw new SampleSellerRuntimeException("Failed to convert SellerAccessTokenRequestJson to JSON string.", e);
        }
        RequestWrapper requestWrapper = new RequestWrapper(sellerAccessTokenRequestString);
        Client client = ClientBuilder
                .newClient()
                .register(requestWrapper);

        // REQUEST SELLER ACCESS TOKEN
        Response response = client
                .target("https://api.amazon.com/auth/O2/token")
                .request()
                .accept("application/json")
                .post(Entity.json(sellerAccessTokenRequestString));
        // REQUEST SELLER ACCESS TOKEN END

        return new RequestResponseWrapper(requestWrapper, new ResponseWrapper(response));
    }

    /**
     * Retrieve subscriptions information for a customer using your seller access token.
     * <p>
     * See the Subscribe with Amazon Getting Started Guide, 4.2.5.
     *
     * @param customerUserId    The user_id that uniquely identifies your customer within your SWA subscription.
     * @param sellerAccessToken Your access token.
     * @return ResponseViewModel containing subscriptions information.
     */
    public static RequestResponseWrapper requestSubscriptions(String customerUserId, String sellerAccessToken) {
        RequestWrapper requestWrapper = new RequestWrapper("");
        Client client = ClientBuilder
                .newClient()
                .register(requestWrapper);

        // REQUEST SUBSCRIPTIONS
        Response response = client
                .target("https://appstore-sdk.amazon.com/v3/subscriptions")
                .queryParam("beneficiaryId", customerUserId)
                .queryParam("beneficiaryType", "LWA_USER_ID")
                .request()
                .header("Authorization", "bearer " + sellerAccessToken)
                .accept("application/json")
                .get();
        // REQUEST SUBSCRIPTIONS END

        return new RequestResponseWrapper(requestWrapper, new ResponseWrapper(response));
    }
}
