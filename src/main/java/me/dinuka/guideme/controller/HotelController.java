package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.service.HotelService;
import me.dinuka.guideme.service.ListingService;

import java.util.HashMap;

@Path("/hotels")
public class HotelController {

    @GET
    @Path("/{key}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMainHotels(@PathParam("key") String key){
        return new HotelService().getMainHotels(key);
    }

    @POST
    @Path("/checkavailability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailability(HashMap<String, Object> hashMap){
        return new HotelService().checkAvailability(hashMap);
    }

}
