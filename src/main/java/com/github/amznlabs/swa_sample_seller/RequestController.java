package com.github.amznlabs.swa_sample_seller;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

/**
 * Routes user requests to response handlers.
 */
@Path("/")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    /**
     * Landing page.
     *
     * @return HTTP response
     * @throws IOException
     */
    @GET
    @Path("/")
    public String index() throws IOException {
        return renderTemplate("landing", Config.getInstance());
    }

    /**
     * Render a Handlebars template from the resources folder.
     *
     * @param templatePath Path to template without the extension.
     * @param data         Data to bind to the template.
     * @return Template rendered with data.
     */
    public static String renderTemplate(String templatePath, Object data) {
        try {
            Handlebars handlebars = Config.getInstance().getHandlebars();
            Template template = handlebars.compile(templatePath);
            String result = template.apply(Context.newBuilder(data).resolver(
                    FieldValueResolver.INSTANCE,
                    MethodValueResolver.INSTANCE,
                    JavaBeanValueResolver.INSTANCE,
                    MapValueResolver.INSTANCE)
                    .build());
            return result;
        } catch (IOException e) {
            logger.error("Failed to render template with path {}: {}", templatePath, e);
            return "";
        }
    }
}
