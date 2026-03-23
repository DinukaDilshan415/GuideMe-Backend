package me.dinuka.guideme.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.dto.UserAddressDTO;
import me.dinuka.guideme.dto.UserDTO;
import me.dinuka.guideme.service.PartnerService;
import me.dinuka.guideme.service.UserService;

@Path("/user")
public class UserController {
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDTO dto){

        return new UserService().registerUser(dto);
    }

    @GET
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUserData(@PathParam("email") String email){
        return new UserService().getCurrentUserData(email);
    }

    @POST
    @Path("/saveorupdateaddress")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userAddressSaveOrUpdate(UserAddressDTO dto){
        return new UserService().userAddressSaveOrUpdate(dto);
    }

    @PUT
    @Path("/updatemobile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userUpdateMobile(UserDTO dto){
        return new UserService().userUpdateMobile(dto);
    }

    @PUT
    @Path("/updatenameandimage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userUpdateNameAndImage(UserDTO dto){
        return new UserService().userUpdateNameAndImage(dto);
    }

    @PUT
    @Path("/updatecountry")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userUpdateCountry(UserDTO dto){
        return new UserService().userUpdateCountry(dto);
    }

    @GET
    @Path("/address/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUserAddressData(@PathParam("email") String email){
        return new UserService().getCurrentUserAddressData(email);
    }

}
