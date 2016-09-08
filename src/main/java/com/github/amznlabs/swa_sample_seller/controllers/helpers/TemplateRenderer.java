package com.github.amznlabs.swa_sample_seller.controllers.helpers;

import com.github.amznlabs.swa_sample_seller.Config;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * TemplateRenderer provides helper methods to render Handlebars templates.
 */
public class TemplateRenderer {
    private static final Logger logger = LoggerFactory.getLogger(TemplateRenderer.class);

    /**
     * @return A Handlebars context with useful default resolvers.
     */
    public static Context.Builder newContextBuilder() {
        return Context.newBuilder(null).resolver(
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                MapValueResolver.INSTANCE);
    }

    /**
     * Compile a Handlebars template.
     *
     * @param path Relative to Config.RESOURCE_PATH/Config.HANDLEBARS_DIRECTORY_PATH
     * @return Handlebars template.
     * @throws IOException If no template was found at path.
     */
    public static Template compile(String path) throws IOException {
        return Config.getInstance().getHandlebars().compile(path);
    }
}
