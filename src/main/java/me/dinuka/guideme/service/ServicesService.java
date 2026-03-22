package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.entity.*;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServicesService {
    public Response getMainServices(String key) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<ServiceListing> serviceListings = session.createQuery("FROM ServiceListing sl", ServiceListing.class)
                .getResultList();

        if (serviceListings.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        List<HashMap<String, Object>> dto = new ArrayList<>();
        for (ServiceListing serviceListing : serviceListings) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", serviceListing.getId());
            map.put("per_day_bookings", serviceListing.getPer_day_bookings());
            map.put("title", serviceListing.getListing().getTitle());
            map.put("description", serviceListing.getListing().getDescription());
            map.put("address", serviceListing.getListing().getAddress());
            map.put("price", serviceListing.getListing().getPrice());
            map.put("latitude", serviceListing.getListing().getLatitude());
            map.put("longitude", serviceListing.getListing().getLongitude());
            map.put("location_name", serviceListing.getListing().getLocation_name());
            map.put("created_at", serviceListing.getListing().getCreated_at().toString());
            map.put("partner_name", serviceListing.getListing().getPartner().getName());
            map.put("partnerEmail", serviceListing.getListing().getPartner().getEmail());
            map.put("partnerMobile", serviceListing.getListing().getPartner().getMobile());
            map.put("partnerImageUrl", serviceListing.getListing().getPartner().getImage_url());
            map.put("partnerCountry", serviceListing.getListing().getPartner().getCountry());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date createdAt = serviceListing.getListing().getPartner().getCreated_at();

            map.put("partnerCreated_at", sdf.format(createdAt));

            List<ListingImages> listingImages = session.createQuery("FROM ListingImages li WHERE li.listing.id=:list_id ", ListingImages.class)
                    .setParameter("list_id", serviceListing.getListing().getId())
                    .getResultList();

            List<String> imageUris = new ArrayList<>();
            for (ListingImages listingImage : listingImages) {
                imageUris.add(listingImage.getUrl());
            }
            map.put("imageUris", imageUris);

            List<Qualifications> qualifications = session.createQuery("FROM Qualifications q WHERE q.serviceListing.id=:list_id ", Qualifications.class)
                    .setParameter("list_id", serviceListing.getId())
                    .getResultList();

            List<String> qualificationsList = new ArrayList<>();
            for (Qualifications qualification : qualifications) {
                qualificationsList.add(qualification.getName());
            }
            map.put("qualifications", qualificationsList);

            List<Reviews> reviews = session.createQuery("FROM Reviews r WHERE r.listing.id=:list_id ", Reviews.class)
                    .setParameter("list_id", serviceListing.getListing().getId())
                    .getResultList();

            if (reviews.isEmpty()) {
                map.put("rating", 0);
                map.put("reviewCount", 0);
            } else {
                double rating = 0;
                int reviewCount = 0;
                for (Reviews review : reviews) {
                    rating += review.getRating();
                    reviewCount++;
                }
                map.put("rating", rating / reviewCount);
                map.put("reviewCount", reviewCount);
            }

            dto.add(map);
        }

        session.close();
        if (!dto.isEmpty()) {
            return Response.ok(dto).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
