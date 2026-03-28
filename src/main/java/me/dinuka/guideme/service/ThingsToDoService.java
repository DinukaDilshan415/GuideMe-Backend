package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.FacilityDTO;
import me.dinuka.guideme.entity.*;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ThingsToDoService {
    public Response getMainThingsToDo(String key) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<ThingsToDoListing> thingsToDoListings = session.createQuery("FROM ThingsToDoListing ttd", ThingsToDoListing.class)
                .getResultList();

        if (thingsToDoListings.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        List<HashMap<String, Object>> dto = new ArrayList<>();
        for (ThingsToDoListing ttd : thingsToDoListings) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", ttd.getId());
            map.put("l_id", ttd.getListing().getId());
            map.put("categoryId", 2);
            map.put("per_day_bookings", ttd.getPer_day_bookings());
            map.put("guest_requirements", ttd.getGuest_requerements());
            map.put("activity_level", ttd.getActivity_level());
            map.put("what_to_bring", ttd.getWhat_to_bring());
            map.put("title", ttd.getListing().getTitle());
            map.put("description", ttd.getListing().getDescription());
            map.put("address", ttd.getListing().getAddress());
            map.put("price", ttd.getListing().getPrice());
            map.put("latitude", ttd.getListing().getLatitude());
            map.put("longitude", ttd.getListing().getLongitude());
            map.put("location_name", ttd.getListing().getLocation_name());
            map.put("created_at", ttd.getListing().getCreated_at().toString());
            map.put("partner_name", ttd.getListing().getPartner().getName());
            map.put("partnerEmail", ttd.getListing().getPartner().getEmail());
            map.put("partnerMobile", ttd.getListing().getPartner().getMobile());
            map.put("partnerImageUrl", ttd.getListing().getPartner().getImage_url());
            map.put("partnerCountry", ttd.getListing().getPartner().getCountry());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date createdAt = ttd.getListing().getPartner().getCreated_at();

            map.put("partnerCreated_at", sdf.format(createdAt));

            List<ListingImages> listingImages = session.createQuery("FROM ListingImages li WHERE li.listing.id=:list_id ", ListingImages.class)
                    .setParameter("list_id", ttd.getListing().getId())
                    .getResultList();

            List<String> imageUris = new ArrayList<>();
            for (ListingImages listingImage : listingImages) {
                imageUris.add(listingImage.getUrl());
            }
            map.put("imageUris", imageUris);

            List<Reviews> reviews = session.createQuery("FROM Reviews r WHERE r.listing.id=:list_id ", Reviews.class)
                    .setParameter("list_id", ttd.getListing().getId())
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
                map.put("rating", Math.round((rating / reviewCount) * 10.0) / 10.0);
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

    public Response checkAvailability(HashMap<String, Object> hashMap) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        HashMap<String, Object> map = new HashMap<>();

        try {
            int id = Integer.parseInt(hashMap.get("thingstodo_id").toString());
            int guest_count = Integer.parseInt(hashMap.get("guest_count").toString());
            String date_in_str = hashMap.get("date_in").toString();
            String date_out_str = hashMap.get("date_out").toString();

            System.out.println("guest_count: " + guest_count);

            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

            // Convert String dates to java.util.Date objects
            Date date_in = DATE_FORMAT.parse(date_in_str);
            Date date_out = DATE_FORMAT.parse(date_out_str);

            ThingsToDoListing thingsToDoListing = session.createQuery("FROM ThingsToDoListing tdl WHERE tdl.id=:id", ThingsToDoListing.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();

            List<Booking> bookings = session.createQuery("FROM Booking b WHERE b.listing.id=:l_id AND b.date_in=:start", Booking.class)
                    .setParameter("l_id", thingsToDoListing.getListing().getId())
                    .setParameter("start", date_in)
                    .getResultList();

            if (!bookings.isEmpty()) {
                int filledSlots = bookings.size();
                int availableSlots = thingsToDoListing.getPer_day_bookings() - filledSlots;

                System.out.println("Available Slots: " + availableSlots);

                if (availableSlots < 1) {
                    map.put("success", false);
                    map.put("message", "This date is already booked. Please choose another date.");
                    return Response.ok(map).build();
                } else {
                    map.put("success", true);
                    map.put("message", "Available for booking");
                    return Response.ok(map).build();
                }
            } else {
                    map.put("success", true);
                    map.put("message", "Available for booking");
                    return Response.ok(map).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }
}
