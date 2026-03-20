package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.entity.*;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    public Response addNewListing(HashMap<String, Object> hashMap) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String email = hashMap.get("email").toString();
            String title = hashMap.get("title").toString();
            String description = hashMap.get("description").toString();
            String address = hashMap.get("address").toString();
            double price = Double.parseDouble(hashMap.get("price").toString());
            double latitude = Double.parseDouble(hashMap.get("latitude").toString());
            double longitude = Double.parseDouble(hashMap.get("longitude").toString());
            String locationName = hashMap.get("locationName").toString();
            int categoryId = Integer.parseInt(hashMap.get("categoryId").toString());
            int adultCount = Integer.parseInt(hashMap.get("adultCount").toString());
            int roomCount = Integer.parseInt(hashMap.get("roomCount").toString());
            ArrayList<Integer> selectedFacilities = (ArrayList<Integer>) hashMap.get("selectedFacilities");
            List<String> roomTypes = (List<String>) hashMap.get("roomTypes");
            List<String> imageUris = (List<String>) hashMap.get("imageUris");

            Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();

            Status status = session.get(Status.class, 1);
            ListingType listingType = session.get(ListingType.class, categoryId);

            List<Listing> listings = session.createQuery("FROM Listing l ORDER BY l.id DESC", Listing.class)
                    .getResultList();

            String listingId;
            if (listings.isEmpty()) {
                listingId = "#HTL01";
            } else {
                 listingId = "#HTL0" + (listings.get(0).getId() + 1);
            }

            Listing listing = new Listing();
            listing.setList_id(listingId);
            listing.setTitle(title);
            listing.setDescription(description);
            listing.setAddress(address);
            listing.setPrice(price);
            listing.setLatitude(latitude);
            listing.setLongitude(longitude);
            listing.setLocation_name(locationName);
            listing.setListing_type(listingType);
            listing.setPartner(partner);
            listing.setStatus(status);

            Date now = new Date();
            listing.setCreated_at(now);
            listing.setUpdated_at(now);

            session.persist(listing);

            HotelListing hotelListing = new HotelListing();
            hotelListing.setListing(listing);
            hotelListing.setPer_room_count(adultCount);
            hotelListing.setRooms_count(roomCount);

            session.persist(hotelListing);

            for (String roomType : roomTypes) {
                RoomTypes roomTypes1 = new RoomTypes();
                roomTypes1.setType(roomType);
                roomTypes1.setHotelListing(hotelListing);
                session.persist(roomTypes1);
            }

            for (String imageUri : imageUris) {
                ListingImages listingImages = new ListingImages();
                listingImages.setUrl(imageUri);
                listingImages.setListing(listing);
                session.persist(listingImages);
            }

            for (Integer facilityId : selectedFacilities) {
                HotelListingHasFacilities hotelListingHasFacilities = new HotelListingHasFacilities();
                hotelListingHasFacilities.setHotel_listing(hotelListing);
                Facility facility = session.get(Facility.class, facilityId);
                hotelListingHasFacilities.setFacility(facility);
                session.persist(hotelListingHasFacilities);
            }

            session.beginTransaction().commit();

            HashMap<String, Object> map = new HashMap<>();
            map.put("message", "Listing Added Successfully");
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }
}
