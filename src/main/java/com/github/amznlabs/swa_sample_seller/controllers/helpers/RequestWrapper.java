package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.github.amznlabs.swa_sample_seller.utilities.JsonUtils;
import com.github.amznlabs.swa_sample_seller.utilities.UriUtils;
import com.github.amznlabs.swa_sample_seller.viewmodels.RequestViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * Used to catch and wrap outgoing client requests.
 */
public class RequestWrapper implements ClientRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);

    private ClientRequestContext clientRequestContext;
    private String body;

    /**
     * @param body The body of the request.
     */
    public RequestWrapper(String body) {
        this.body = body;
    }

    /**
     * Called by JAX-RS client when request is sent.
     * Saves request information for later use.
     *
     * @param clientRequestContext Information about the request.
     */
    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        this.clientRequestContext = clientRequestContext;
    }

    /**
     * @return Body of the HTTP request.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * @return Human-readable dump of the raw HTTP request.
     */
    public String dump() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("%s %s\n", clientRequestContext.getMethod(), clientRequestContext.getUri().toString()));
        for (String key : clientRequestContext.getHeaders().keySet()) {
            String valuesList = clientRequestContext.getHeaderString(key);
            stringBuffer.append(String.format("%s: %s\n", key, valuesList));
        }
        return stringBuffer.toString().trim() + "\n" + getBody();
    }

    /**
     * Build a RequestViewModel from this request.
     */
    public RequestViewModel getRequestViewModel() {
        RequestViewModel requestViewModel = new RequestViewModel();
        requestViewModel.setMethod(clientRequestContext.getMethod());
        requestViewModel.setTarget(clientRequestContext.getUri().toString());
        requestViewModel.setQueryParameters(UriUtils.splitQuery(clientRequestContext.getUri()));
        for (String key : clientRequestContext.getHeaders().keySet()) {
            String valuesList = clientRequestContext.getHeaderString(key);
            requestViewModel.getHeaders().put(key, valuesList);
        }
        requestViewModel.setBody(JsonUtils.prettyPrintJson(getBody()));
        return requestViewModel;
    }
}
