// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SellerAccessTokenRequestJson {
    @JsonProperty("grant_type")
    private String grantType;

    private String scope;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    public SellerAccessTokenRequestJson(String clientId, String clientSecret) {
        this.grantType = "client_credentials";
        this.scope = "swa:subscriptions";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
