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
            user.setCountry(dto.getCountry());

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

    public Response getCurrentUserData(String email){
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        session.close();

        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setMobile(user.getMobile());
        dto.setOtp(user.getOtp());
        dto.setImageUrl(user.getImage_url());
        dto.setCountry(user.getCountry());
        dto.setCreatedAt(String.valueOf(user.getCreated_at()));
        dto.setUpdatedAt(String.valueOf(user.getUpdated_at()));
        dto.setStatusId(user.getStatus().getId());

        return Response.ok(dto).build();
    }
}
