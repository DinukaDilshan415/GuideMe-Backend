package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.dto.UserDTO;
import me.dinuka.guideme.entity.Partner;
import me.dinuka.guideme.entity.PartnerTypes;
import me.dinuka.guideme.entity.Status;
import me.dinuka.guideme.entity.User;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.HashMap;

public class UserService {
    public Response registerUser(UserDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Status status = session.get(Status.class, dto.getStatusId());

            if (status == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid Status").build();
            }

            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setMobile(dto.getMobile());
            user.setOtp(dto.getOtp());
            user.setImage_url(dto.getImageUrl());

            Date now = new Date();
            user.setCreated_at(now);
            user.setUpdated_at(now);

            user.setStatus(status);

            session.persist(user);

            session.beginTransaction().commit();

            HashMap<String, Object> map = new HashMap<>();
            map.put("message", "User Registered Successfully");
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }
}
