package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.service.PartnerService;

@Path("/partner")
public class PartnerController {
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerPartner(PartnerDTO dto){
        return new PartnerService().registerPartner(dto);
    }

    @POST
    @Path("/upgradeplan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerUpgradePlan(PartnerDTO dto){
        return new PartnerService().partnerUpgradePlan(dto);
    }

    @POST
    @Path("/cancelplan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerCancelPlan(PartnerDTO dto){
        return new PartnerService().partnerCancelPlan(dto);
    }

    @POST
    @Path("/saveorupdateaddress")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerAddressSaveOrUpdate(PartnerAddressDTO dto){
        return new PartnerService().partnerAddressSaveOrUpdate(dto);
    }

    @PUT
    @Path("/updatemobile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerUpdateMobile(PartnerDTO dto){
        return new PartnerService().partnerUpdateMobile(dto);
    }

    @PUT
    @Path("/updatenameandimage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerUpdateNameAndImage(PartnerDTO dto){
        return new PartnerService().partnerUpdateNameAndImage(dto);
    }

    @PUT
    @Path("/updatecountry")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response partnerUpdateCountry(PartnerDTO dto){
        return new PartnerService().partnerUpdateCountry(dto);
    }

    @GET
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentPartnerData(@PathParam("email") String email){
        return new PartnerService().getCurrentPartnerData(email);
    }

    @GET
    @Path("/address/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentPartnerAddressData(@PathParam("email") String email){
        return new PartnerService().getCurrentPartnerAddressData(email);
    }

    @GET
    @Path("/checkpartnership/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPartnership(@PathParam("email") String email){
        return new PartnerService().checkPartnership(email);
    }

}
