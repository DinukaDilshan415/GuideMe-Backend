package me.dinuka.guideme.service;

import jakarta.ws.rs.core.Response;
import me.dinuka.guideme.dto.PartnerDTO;
import me.dinuka.guideme.entity.*;
import me.dinuka.guideme.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
            List<String> imageUris = (List<String>) hashMap.get("imageUris");

            Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();

            Status status = session.get(Status.class, 1);
            ListingType listingType = session.get(ListingType.class, categoryId);

            List<Listing> listings = session.createQuery("FROM Listing l WHERE l.listing_type.id=:type_id ORDER BY l.id DESC", Listing.class)
                    .setParameter("type_id", categoryId)
                    .getResultList();

            String listingId = "#";
            if (categoryId == 1){ //hotel

                if (listings.isEmpty()) {
                    listingId = "#HTL01";
                } else {
                    int lId = listings.get(0).getId();
                    List<HotelListing> hotelListings = session.createQuery("FROM HotelListing hl WHERE hl.listing.id=:list_id ORDER BY hl.id DESC", HotelListing.class)
                            .setParameter("list_id", lId)
                            .getResultList();
                    if (hotelListings.isEmpty()) {
                        listingId = "#HTL01";
                    }
                    listingId = "#HTL0" + ( hotelListings.get(0).getId()+ 1);
                }

            } else if (categoryId == 2){ //things to do

                if (listings.isEmpty()) {
                    listingId = "#TTD01";
                } else {
                    int lId = listings.get(0).getId();
                    List<ThingsToDoListing> thingsToDoListings = session.createQuery("FROM ThingsToDoListing ttd WHERE ttd.listing.id=:list_id ORDER BY ttd.id DESC", ThingsToDoListing.class)
                            .setParameter("list_id", lId)
                            .getResultList();
                    if (thingsToDoListings.isEmpty()) {
                        listingId = "#TTD01";
                    }
                    listingId = "#TTD0" + (thingsToDoListings.get(0).getId() + 1);
                }

            } else if (categoryId == 3){ //services

                if (listings.isEmpty()) {
                    listingId = "#SVC01";
                } else {
                    int lId = listings.get(0).getId();
                    List<ServiceListing> serviceListings = session.createQuery("FROM ServiceListing sl WHERE sl.listing.id=:list_id ORDER BY sl.id DESC", ServiceListing.class)
                            .setParameter("list_id", lId)
                            .getResultList();
                    if (serviceListings.isEmpty()) {
                        listingId = "#SVC01";
                    }
                    listingId = "#SVC0" + (serviceListings.get(0).getId() + 1);
                }
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

            if (categoryId == 1){ //hotel

                int adultCount = Integer.parseInt(hashMap.get("adultCount").toString());
                int roomCount = Integer.parseInt(hashMap.get("roomCount").toString());
                ArrayList<Integer> selectedFacilities = (ArrayList<Integer>) hashMap.get("selectedFacilities");
                List<String> roomTypes = (List<String>) hashMap.get("roomTypes");

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

            } else if (categoryId == 2){ //things to do

                int bookingCount = Integer.parseInt(hashMap.get("bookingCount").toString());
                String listingGR = hashMap.get("listingGR").toString();
                String listingAL = hashMap.get("listingAL").toString();
                String listingWB = hashMap.get("listingWB").toString();

                ThingsToDoListing thingsToDoListing = new ThingsToDoListing();
                thingsToDoListing.setListing(listing);
                thingsToDoListing.setPer_day_bookings(bookingCount);
                thingsToDoListing.setGuest_requerements(listingGR);
                thingsToDoListing.setActivity_level(listingAL);
                thingsToDoListing.setWhat_to_bring(listingWB);

                session.persist(thingsToDoListing);

                for (String imageUri : imageUris) {
                    ListingImages listingImages = new ListingImages();
                    listingImages.setUrl(imageUri);
                    listingImages.setListing(listing);

                    session.persist(listingImages);
                }

            } else if (categoryId == 3){ //services
                int bookingCount = Integer.parseInt(hashMap.get("bookingCount").toString());
                List<String> qualifications = (List<String>) hashMap.get("qualifications");

                ServiceListing serviceListing = new ServiceListing();
                serviceListing.setListing(listing);
                serviceListing.setPer_day_bookings(bookingCount);

                session.persist(serviceListing);

                for (String imageUri : imageUris) {
                    ListingImages listingImages = new ListingImages();
                    listingImages.setUrl(imageUri);
                    listingImages.setListing(listing);

                    session.persist(listingImages);
                }

                for (String qualification: qualifications) {
                    Qualifications qualifications1 = new Qualifications();
                    qualifications1.setServiceListing(serviceListing);
                    qualifications1.setName(qualification);
                    session.persist(qualifications1);
                }
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

    public Response getPartnerListings(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
//        Partner partner = session.createQuery("FROM Partner p WHERE p.email=:email", Partner.class)
//                .setParameter("email", email)
//                .getSingleResultOrNull();

        List<Listing> listings = session.createQuery("FROM Listing l WHERE l.partner.email=:email ORDER BY l.created_at DESC", Listing.class)
                .setParameter("email", email)
                .getResultList();

        if (listings.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        List<HashMap<String, Object>> dto = new ArrayList<>();
        for (Listing listing : listings) {
            HashMap<String, Object> map = new HashMap<>();

            List<ListingImages> listingImages = session.createQuery("FROM ListingImages li WHERE li.listing.id=:list_id ", ListingImages.class)
                    .setParameter("list_id", listing.getId())
                    .getResultList();

            map.put("id", listing.getId());
            map.put("list_id", listing.getList_id());
            map.put("title", listing.getTitle());
            map.put("description", listing.getDescription());
            map.put("address", listing.getAddress());
            map.put("price", listing.getPrice());
            map.put("listing_type_id", listing.getListing_type().getId());
            map.put("listing_type",listing.getListing_type().getType());
            map.put("status_id", listing.getStatus().getId());
            map.put("status", listing.getStatus().getType());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm a");
            Date createdAt = listing.getCreated_at();
            map.put("created_at", sdf.format(createdAt));
            map.put("location_name", listing.getLocation_name());
            map.put("image", listingImages.get(0).getUrl());
            dto.add(map);
        }

        session.close();

        return Response.ok(dto).build();
    }
}
