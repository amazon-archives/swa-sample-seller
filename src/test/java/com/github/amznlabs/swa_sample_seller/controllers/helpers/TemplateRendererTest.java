// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Template;
import lombok.Data;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TemplateRendererTest {
    @Test
    public void validTemplate() {
        Context context =
                TemplateRenderer
                        .newContextBuilder()
                        .combine("myClass", new SimpleClass())
                        .combine("myInteger", 0)
                        .combine("myString", "lorem")
                        .build();

        Template template = null;
        try {
            template = TemplateRenderer.compile("test/test");
        } catch (IOException e) {
            throw new RuntimeException("Could not load template", e);
        }

        String result = null;
        try {
            result = template.apply(context);
        } catch (IOException e) {
            throw new RuntimeException("Could not bind data to a template", e);
        }

        Assert.assertEquals("0 * 1 hello world 1 / lorem", result.trim(), "Template did not render properly");
    }

    @Test(expectedExceptions = IOException.class)
    public void missingTemplate() throws IOException {
        TemplateRenderer
                .compile("test/missing")
                .apply(TemplateRenderer.newContextBuilder().build());
    }
}

@Data
class SimpleClass {
    private Integer integer = 1;
    private String string = "hello world";
}
