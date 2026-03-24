package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.service.HotelService;
import me.dinuka.guideme.service.ThingsToDoService;

import java.util.HashMap;

@Path("/thingstodo")
public class ThingsToDoController {

    @GET
    @Path("/{key}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMainThingsToDo(@PathParam("key") String key){
        return new ThingsToDoService().getMainThingsToDo(key);
    }

    @POST
    @Path("/checkavailability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailability(HashMap<String, Object> hashMap){
        return new ThingsToDoService().checkAvailability(hashMap);
    }

}
