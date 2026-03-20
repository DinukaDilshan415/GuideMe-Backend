package me.dinuka.guideme.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.service.FacilityService;
import me.dinuka.guideme.service.ListingService;

@Path("/listing")
public class ListingController {
    @GET
    @Path("/types/getall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListingTypes(){
        return new ListingService().getAllListingTypes();
    }
}
