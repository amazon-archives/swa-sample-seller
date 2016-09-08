package com.github.amznlabs.swa_sample_seller.viewmodels;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ResponseViewModel {
    private int status = 0;
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body;
}
