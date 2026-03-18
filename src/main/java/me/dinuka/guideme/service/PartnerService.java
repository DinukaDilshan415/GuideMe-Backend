package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.entity.Partner;
import me.dinuka.guideme.entity.PartnerTypes;
import me.dinuka.guideme.entity.Status;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.HashMap;

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
}
