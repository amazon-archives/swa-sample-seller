// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SellerAccessTokenResponseJson {

    @JsonProperty("access_token")
    private String accessToken;

    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;
}
