package com.github.amznlabs.swa_sample_seller.viewmodels;

import lombok.Data;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class RequestViewModel {
    private  String method;
    private String target;
    private Map<String, String> queryParameters = new LinkedHashMap<>();
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body;
}
