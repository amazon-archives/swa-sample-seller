package main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

/**
 * Routes user requests to response handlers.
 */
@Path("/")
public class Resource {

    /**
     * Landing page.
     * @return HTTP response
     * @throws IOException
     */
    @GET
    @Path("/")
    public String index() throws IOException {
        return "hello world";
    }
}
