package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerAddressDTO;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.entity.Partner;
import me.dinuka.guideme.entity.PartnerAddress;
import me.dinuka.guideme.entity.PartnerTypes;
import me.dinuka.guideme.entity.Status;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PartnerService {
    public Response registerPartner(PartnerDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Status status = session.get(Status.class, dto.getStatusId());
            PartnerTypes partnerType = session.get(PartnerTypes.class, dto.getPartnerTypeId());

            if (status == null || partnerType == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid Status or Partner Type ID").build();
            }

            Partner partner = new Partner();
            partner.setName(dto.getName());
            partner.setEmail(dto.getEmail());
            partner.setMobile(dto.getMobile());
            partner.setOtp(dto.getOtp());
            partner.setImage_url(dto.getImageUrl());
            partner.setCountry(dto.getCountry());

            Date now = new Date();
            partner.setCreated_at(now);
            partner.setUpdated_at(now);

            partner.setStatus(status);
            partner.setPartner_types(partnerType);

            session.persist(partner);

            session.beginTransaction().commit();

            HashMap<String, Object> map = new HashMap<>();
            map.put("message", "Partner Registered Successfully");
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response getCurrentPartnerData(String email){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        session.close();

        PartnerDTO dto = new PartnerDTO();
        dto.setEmail(partner.getEmail());
        dto.setName(partner.getName());
        dto.setMobile(partner.getMobile());
        dto.setOtp(partner.getOtp());
        dto.setImageUrl(partner.getImage_url());
        dto.setCountry(partner.getCountry());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date createdAt = partner.getCreated_at();

        dto.setCreatedAt(sdf.format(createdAt));
        dto.setUpdatedAt(String.valueOf(partner.getUpdated_at()));
        dto.setStatusId(partner.getStatus().getId());
        dto.setPartnerTypeId(partner.getPartner_types().getId());

        return Response.ok(dto).build();
    }

    public Response getCurrentPartnerAddressData(String email){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        List<PartnerAddress> addressList = session.createQuery("FROM PartnerAddress pa WHERE pa.partner.id = :partnerId", PartnerAddress.class)
                .setParameter("partnerId", partner.getId())
                .getResultList();

        session.close();

        if (!addressList.isEmpty()){
            PartnerAddress partnerAddress = addressList.get(0);

            PartnerAddressDTO dto = new PartnerAddressDTO();
            dto.setAddress(partnerAddress.getAddress());
            dto.setCity(partnerAddress.getCity());
            dto.setPostalCode(partnerAddress.getPostal_code());

            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    public Response partnerUpdateMobile(PartnerDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getMobile() != null && dto.getEmail() != null){
                Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                partner.setMobile(dto.getMobile());
                Date now = new Date();
                partner.setUpdated_at(now);

                session.merge(partner);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "Partner Mobile Updated Successfully");
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

    public Response partnerUpdateNameAndImage(PartnerDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getName() != null && dto.getEmail() != null && dto.getImageUrl() != null){
                Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                partner.setName(dto.getName());
                partner.setImage_url(dto.getImageUrl());
                Date now = new Date();
                partner.setUpdated_at(now);

                session.merge(partner);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "Partner Updated Successfully");
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

    public Response partnerUpdateCountry(PartnerDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if(dto.getCountry() != null && dto.getEmail() != null){
                Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                        .setParameter("email", dto.getEmail())
                        .getSingleResultOrNull();

                partner.setCountry(dto.getCountry());
                Date now = new Date();
                partner.setUpdated_at(now);

                session.merge(partner);

                session.beginTransaction().commit();

                HashMap<String, Object> map = new HashMap<>();
                map.put("message", "Partner Country Updated Successfully");
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

    public Response partnerAddressSaveOrUpdate(PartnerAddressDTO dto){
        Session session = HibernateUtil.getSessionFactory().openSession();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                    .setParameter("email", dto.getPartnerEmail())
                    .getSingleResultOrNull();

            List<PartnerAddress> addressList = session.createQuery("FROM PartnerAddress pa WHERE pa.partner.id = :partnerId", PartnerAddress.class)
                    .setParameter("partnerId", partner.getId())
                    .getResultList();

            if (addressList.isEmpty()){
                PartnerAddress partnerAddress = new PartnerAddress();
                partnerAddress.setAddress(dto.getAddress());
                partnerAddress.setCity(dto.getCity());
                partnerAddress.setPostal_code(dto.getPostalCode());
                partnerAddress.setPartner(partner);

                session.persist(partnerAddress);

                map.put("message", "Partner Address Saved Successfully");
            } else {
                PartnerAddress partnerAddress = addressList.get(0);

                partnerAddress.setAddress(dto.getAddress());
                partnerAddress.setCity(dto.getCity());
                partnerAddress.setPostal_code(dto.getPostalCode());

                session.merge(partnerAddress);

                map.put("message", "Partner Address Updated Successfully");
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
}
