package com.github.amznlabs.swa_sample_seller.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiDumpViewModel {
    private RequestViewModel customerProfileRequest = new RequestViewModel();
    private Boolean customerProfileRequestSucceeded = true;
    private ResponseViewModel customerProfileResponse = new ResponseViewModel();

    private RequestViewModel sellerAccessTokenRequest = new RequestViewModel();
    private Boolean sellerAccessTokenRequestSucceeded = true;
    private ResponseViewModel sellerAccessTokenResponse = new ResponseViewModel();

    private RequestViewModel subscriptionsRequest = new RequestViewModel();
    private Boolean subscriptionsRequestSucceeded = true;
    private ResponseViewModel subscriptionsResponse = new ResponseViewModel();
}
