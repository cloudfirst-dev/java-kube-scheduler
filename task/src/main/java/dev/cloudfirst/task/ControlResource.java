package dev.cloudfirst.task;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.runtime.Quarkus;

@Path("/task")
public class ControlResource {

    @POST
    @Path("/shutdown")
    @Produces(MediaType.TEXT_PLAIN)
    public String shutdown() {
        Quarkus.asyncExit(0);
        return "hello";
    }
}