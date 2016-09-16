// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import lombok.Data;

/**
 * POJO to wrap RequestWrapper and ResponseWrapper.
 */
@Data
public class RequestResponseWrapper {
    private final RequestWrapper requestWrapper;
    private final ResponseWrapper responseWrapper;

    /**
     * Construct a RequestResponseWrapper object.
     */
    public RequestResponseWrapper(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        this.requestWrapper = requestWrapper;
        this.responseWrapper = responseWrapper;
    }
}
