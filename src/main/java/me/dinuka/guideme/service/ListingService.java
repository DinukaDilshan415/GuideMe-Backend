package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.entity.Facility;
import me.dinuka.guideme.entity.ListingType;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ListingService {
    public Response getAllListingTypes(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<ListingType> listingTypes = session.createQuery("FROM ListingType lt", ListingType.class)
                .getResultList();
        session.close();

        if(listingTypes.size() > 0){
            return Response.ok(listingTypes).build();
        }else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
