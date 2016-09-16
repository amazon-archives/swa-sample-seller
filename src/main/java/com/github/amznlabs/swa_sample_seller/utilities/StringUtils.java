// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.utilities;

public class StringUtils {

    /**
     * Sort a string by character.
     *
     * Adapated from http://stackoverflow.com/questions/605891/sort-a-single-string-in-java.
     */
    public static String sort(String string) {
        return string
                .chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
