// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.github.amznlabs.swa_sample_seller.utilities.JsonUtils;
import com.github.amznlabs.swa_sample_seller.utilities.MapUtils;
import com.github.amznlabs.swa_sample_seller.viewmodels.ResponseViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Wraps a javax.ws.rs.core.Response.
 */
public class ResponseWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseWrapper.class);

    private Response response;
    private String body;

    /**
     * @param response The response we're wrapping.
     */
    public ResponseWrapper(Response response) {
        this.response = response;
        this.body = response.readEntity(String.class);
    }

    /**
     * @return ResponseWrapper object we're wrapping.
     */
    public Response getRawResponse() {
        return response;
    }

    /**
     * @return Body of the HTTP response.
     */
    public String getBody() {
        return body;
    }

    public boolean ok() {
        return response.getStatusInfo().equals(Response.Status.OK);
    }

    /**
     * @return Human-readable dump of the raw HTTP response.
     */
    public String dump() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("%d%n", response.getStatus()));
        MultivaluedMap<String, String> headers = response.getStringHeaders();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String valuesList = String.join(",", entry.getValue());
            stringBuffer.append(String.format("%s: %s%n", entry.getKey(), valuesList));
        }
        stringBuffer.append(getBody());
        return stringBuffer.toString().trim();
    }

    /**
     * Build a ResponseViewModel from this response.
     */
    public ResponseViewModel getResponseViewModel() {
        ResponseViewModel responseViewModel = new ResponseViewModel();
        new ResponseViewModel();
        responseViewModel.setStatus(this.response.getStatus());
        responseViewModel.setHeaders(MapUtils.multivaluedMap2Map(this.response.getStringHeaders()));
        responseViewModel.setBody(JsonUtils.prettyPrintJson(getBody()));
        return responseViewModel;
    }
}
