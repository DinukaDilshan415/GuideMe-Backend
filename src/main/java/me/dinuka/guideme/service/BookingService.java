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

public class BookingService {
//    public Response getAllListingTypes(){
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        List<ListingType> listingTypes = session.createQuery("FROM ListingType lt", ListingType.class)
//                .getResultList();
//        session.close();
//
//        if(listingTypes.size() > 0){
//            return Response.ok(listingTypes).build();
//        }else {
//            return Response.status(Response.Status.NO_CONTENT).build();
//        }
//    }

    public Response addNewBooking(HashMap<String, Object> hashMap) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            int hotelId = ((Number) hashMap.get("hotel_id")).intValue();
            String email = hashMap.get("email").toString();
            String title = hashMap.get("title").toString();
            double price = Double.parseDouble(hashMap.get("price").toString());
            double booking_fee = Double.parseDouble(hashMap.get("booking_fee").toString());

            int categoryId = ((Number) hashMap.get("categoryId")).intValue();
            String date_in = hashMap.get("date_in").toString();
            String date_out = hashMap.get("date_out").toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date_in_date = sdf.parse(date_in);
            Date date_out_date = sdf.parse(date_out);

            User user = session.createQuery("FROM User u WHERE u.email=:email", User.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();

            Status status = session.get(Status.class, 3);

            HotelListing hotelListing = session.createQuery("FROM HotelListing hl WHERE hl.id=:id", HotelListing.class)
                    .setParameter("id", hotelId)
                    .getSingleResultOrNull();

            Listing listing = session.createQuery("FROM Listing l WHERE l.id=:id", Listing.class)
                    .setParameter("id", hotelListing.getListing().getId())
                    .getSingleResultOrNull();

            List<Booking> bookings = session.createQuery("FROM Booking b ORDER BY b.id DESC", Booking.class)
                    .getResultList();

            String bookingId = "#";
                if (bookings.isEmpty()) {
                    bookingId = "#RSV01";
                } else {
                    bookingId = "#RSV0" + ( bookings.get(0).getId()+ 1);
                }

            Booking booking = new Booking();
            booking.setBooking_id(bookingId);
            booking.setTitle(title);
            booking.setPrice(price);
            booking.setBooking_fee(booking_fee);
            booking.setDate_in(date_in_date);
            booking.setDate_out(date_out_date);
            Date now = new Date();
            booking.setCreated_at(now);
            booking.setListing(listing);
            booking.setStatus(status);
            booking.setUser(user);

            session.persist(booking);

            if (categoryId == 1){ //hotel

                int adultCount = Integer.parseInt(hashMap.get("adultCount").toString());
                int childCount = Integer.parseInt(hashMap.get("childCount").toString());
                int roomCount = Integer.parseInt(hashMap.get("roomCount").toString());
                int nights = Integer.parseInt(hashMap.get("nights").toString());
                int roomTypeId = Integer.parseInt(hashMap.get("roomTypeId").toString());

                RoomTypes roomType = session.get(RoomTypes.class, roomTypeId);

                BookingItemHotel bookingItemHotel = new BookingItemHotel();
                bookingItemHotel.setBooking(booking);
                bookingItemHotel.setAdult_count(adultCount);
                bookingItemHotel.setChildren_count(childCount);
                bookingItemHotel.setRoom_count(roomCount);
                bookingItemHotel.setNight_count(nights);
                bookingItemHotel.setRoomTypes(roomType);

                session.persist(bookingItemHotel);

            } else if (categoryId == 2){ //things to do


            } else if (categoryId == 3){ //services

            }

            session.beginTransaction().commit();

            HashMap<String, Object> map = new HashMap<>();
            map.put("message", "Booking Added Successfully");
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            session.close();
        }
    }

    public Response getUserBookings(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();

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
