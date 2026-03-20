package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.service.FacilityService;
import me.dinuka.guideme.service.ListingService;
import me.dinuka.guideme.service.PartnerService;

import java.util.HashMap;

@Path("/listing")
public class ListingController {
    @GET
    @Path("/types/getall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListingTypes(){
        return new ListingService().getAllListingTypes();
    }

    @POST
    @Path("/addnewlisting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewListing(HashMap<String, Object> hashMap){
        return new ListingService().addNewListing(hashMap);
    }
}
