package me.dinuka.guideme.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

}
