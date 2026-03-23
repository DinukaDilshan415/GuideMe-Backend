package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.dto.UserAddressDTO;
import me.dinuka.guideme.dto.UserDTO;
import me.dinuka.guideme.entity.*;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date createdAt = user.getCreated_at();

        dto.setCreatedAt(sdf.format(createdAt));
        dto.setUpdatedAt(String.valueOf(user.getUpdated_at()));
        dto.setStatusId(user.getStatus().getId());

        return Response.ok(dto).build();
    }

    public Response userAddressSaveOrUpdate(UserAddressDTO dto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        HashMap<String, Object> map = new HashMap<>();
        try {
            User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                    .setParameter("email", dto.getUserEmail())
                    .getSingleResultOrNull();

            List<UserAddress> addressList = session.createQuery("FROM UserAddress ua WHERE ua.user.id = :userId", UserAddress.class)
                    .setParameter("userId", user.getId())
                    .getResultList();

            if (addressList.isEmpty()){
                UserAddress userAddress = new UserAddress();
                userAddress.setAddress(dto.getAddress());
                userAddress.setCity(dto.getCity());
                userAddress.setPostal_code(dto.getPostalCode());
                userAddress.setUser(user);

                session.persist(userAddress);

                map.put("message", "User Address Saved Successfully");
            } else {
                UserAddress userAddress = addressList.get(0);

                userAddress.setAddress(dto.getAddress());
                userAddress.setCity(dto.getCity());
                userAddress.setPostal_code(dto.getPostalCode());

                session.merge(userAddress);

                map.put("message", "User Address Updated Successfully");
            }

            session.beginTransaction().commit();

            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response userUpdateMobile(UserDTO dto) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getMobile() != null && dto.getEmail() != null){
                User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                user.setMobile(dto.getMobile());
                Date now = new Date();
                user.setUpdated_at(now);

                session.merge(user);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "User Mobile Updated Successfully");
                return Response.ok(map).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response userUpdateNameAndImage(UserDTO dto) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getName() != null && dto.getEmail() != null && dto.getImageUrl() != null){
                User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                user.setName(dto.getName());
                user.setImage_url(dto.getImageUrl());
                Date now = new Date();
                user.setUpdated_at(now);

                session.merge(user);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "User Updated Successfully");
                return Response.ok(map).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response userUpdateCountry(UserDTO dto) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getCountry() != null && dto.getEmail() != null){
                User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                user.setCountry(dto.getCountry());
                Date now = new Date();
                user.setUpdated_at(now);

                session.merge(user);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "User Country Updated Successfully");
                return Response.ok(map).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response getCurrentUserAddressData(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        List<UserAddress> addressList = session.createQuery("FROM UserAddress ua WHERE ua.user.id = :userId", UserAddress.class)
                .setParameter("userId", user.getId())
                .getResultList();

        session.close();

        if (!addressList.isEmpty()){
            UserAddress userAddress = addressList.get(0);

            UserAddressDTO dto = new UserAddressDTO();
            dto.setAddress(userAddress.getAddress());
            dto.setCity(userAddress.getCity());
            dto.setPostalCode(userAddress.getPostal_code());

            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
