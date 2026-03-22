package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.service.ServicesService;
import me.dinuka.guideme.service.ThingsToDoService;

@Path("/service")
public class ServiceController {

    @GET
    @Path("/{key}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMainServices(@PathParam("key") String key){

        return new ServicesService().getMainServices(key);
    }

}
