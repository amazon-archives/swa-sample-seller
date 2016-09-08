package com.github.amznlabs.swa_sample_seller.models;

public class SellerAccessTokenRequestJson {

    // These need to be non-static so they parse into JSON properly
    public final String grant_type = "client_credentials";
    public final String scope = "swa:subscriptions";

    public String client_id;
    public String client_secret;

    public SellerAccessTokenRequestJson(String client_id, String client_secret) {
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
}
