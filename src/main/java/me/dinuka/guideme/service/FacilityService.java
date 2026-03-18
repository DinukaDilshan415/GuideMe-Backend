package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.entity.Facility;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class FacilityService {
    public Response getAllFacilities(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Facility> facilities = session.createQuery("FROM Facility f", Facility.class)
                .getResultList();
        session.close();

        if(facilities.size() > 0){
            return Response.ok(facilities).build();
        }else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
