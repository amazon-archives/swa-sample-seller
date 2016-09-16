// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerProfileResponseJson {
    private String email;

    private String name;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("user_id")
    private String userId;
}
