// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class StringUtilsTest {
    private final SoftAssert softAssert = new SoftAssert();

    @Test
    public void validSimpleSort() {
        String expected = "abcde";
        String actual = StringUtils.sort("edcba");
        Assert.assertEquals(expected, actual, "expected != actual");
    }

    @Test
    public void validLongSort() {
        String expected = "\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\",,,:::::AACCCDDDEEEEEEEEIIIIIIILLLLLLLLNNNNRRRSSSTTTVV" +
                "__________aaabcccccccddeeeeeeeeegiiiiiiillllnnnnnnooppprrrrsssssssttttttttuwy{}";
        String order1 = "{\"scope\":\"swa:subscriptions\",\"grant_type\":\"client_credentials\"," +
                "\"client_id\":\"INVALID_SELLER_CLIENT_ID\",\"client_secret\":\"INVALID_SELLER_CLIENT_SECRET\"}";
        String order2 = "{\"grant_type\":\"client_credentials\",\"scope\":\"swa:subscriptions\"," +
                "\"client_id\":\"INVALID_SELLER_CLIENT_ID\",\"client_secret\":\"INVALID_SELLER_CLIENT_SECRET\"}";
        String actual1 = StringUtils.sort(order1);
        String actual2 = StringUtils.sort(order2);

        softAssert.assertEquals(expected, actual1, "expected != actual1");
        softAssert.assertEquals(actual1, actual2, "actual1 != actual2");
        softAssert.assertAll();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void nullSort() {
        StringUtils.sort(null);
    }
}
