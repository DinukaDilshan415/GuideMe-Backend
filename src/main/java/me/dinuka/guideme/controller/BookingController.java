package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.service.BookingService;
import me.dinuka.guideme.service.ListingService;

import java.util.HashMap;

@Path("/booking")
public class BookingController {
//    @GET
//    @Path("/types/getall")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllListingTypes(){
//        return new ListingService().getAllListingTypes();
//    }

    @GET
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBookings(@PathParam("email") String email){
        return new BookingService().getUserBookings(email);
    }

    @POST
    @Path("/addnewbooking")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewBooking(HashMap<String, Object> hashMap){
        return new BookingService().addNewBooking(hashMap);
    }
}
