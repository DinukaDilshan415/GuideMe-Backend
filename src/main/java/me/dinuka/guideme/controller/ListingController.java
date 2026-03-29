package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.dto.ReviewDTO;
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

    @GET
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerListings(@PathParam("email") String email){
        return new ListingService().getPartnerListings(email);
    }

    @GET
    @Path("/reviews/{lid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListingReviews(@PathParam("lid") int l_id){
        return new ListingService().getListingReviews(l_id);
    }

    @POST
    @Path("/addnewlisting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewListing(HashMap<String, Object> hashMap){
        return new ListingService().addNewListing(hashMap);
    }

    @POST
    @Path("/addreview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(ReviewDTO reviewDTO){
        return new ListingService().addReview(reviewDTO);
    }
}
