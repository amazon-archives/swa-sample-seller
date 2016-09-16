// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.models;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionsResponseJson {
    private Long nextCursor;
    private List<SubscriptionJson> subscriptions;
}
