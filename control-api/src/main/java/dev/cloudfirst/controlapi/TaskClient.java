package dev.cloudfirst.controlapi;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/task")
@RegisterRestClient
public interface TaskClient {
    @POST
    @Path("/shutdown")
    @Produces(MediaType.TEXT_PLAIN)
    public String shutdown();
}
