package com.github.amznlabs.swa_sample_seller.controllers.helpers;

/**
 * POJO to wrap RequestWrapper and ResponseWrapper.
 */
public class RequestResponseWrapper {
    public final RequestWrapper requestWrapper;
    public final ResponseWrapper responseWrapper;

    /**
     * Construct a RequestResponseWrapper object.
     * @param requestWrapper
     * @param responseWrapper
     */
    public RequestResponseWrapper(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        this.requestWrapper = requestWrapper;
        this.responseWrapper = responseWrapper;
    }
}
