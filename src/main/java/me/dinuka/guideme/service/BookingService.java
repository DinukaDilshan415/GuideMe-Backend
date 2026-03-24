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

        List<Booking> bookings = session.createQuery("FROM Booking b WHERE b.user.email=:email ORDER BY b.created_at DESC", Booking.class)
                .setParameter("email", email)
                .getResultList();

        if (bookings.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        List<HashMap<String, Object>> dto = new ArrayList<>();
        for (Booking booking : bookings) {
            HashMap<String, Object> map = new HashMap<>();

            List<ListingImages> listingImages = session.createQuery("FROM ListingImages li WHERE li.listing.id=:list_id ", ListingImages.class)
                    .setParameter("list_id", booking.getListing().getId())
                    .getResultList();

            map.put("id", booking.getId());
            map.put("booking_id", booking.getBooking_id());
            map.put("title", booking.getTitle());
            map.put("price", booking.getPrice());
            map.put("booking_fee", booking.getBooking_fee());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date_in = booking.getDate_in();
            map.put("date_in", sdf.format(date_in));
            Date date_out = booking.getDate_out();
            map.put("date_out", sdf.format(date_out));
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm a");
            Date createdAt = booking.getCreated_at();
            map.put("created_at", sdf2.format(createdAt));
            map.put("status_id", booking.getStatus().getId());
            map.put("status", booking.getStatus().getType());
            map.put("location_name", booking.getListing().getLocation_name());
            map.put("imageUri", listingImages.get(0).getUrl());

            BookingItemHotel itemHotel = session.createQuery("FROM BookingItemHotel bi WHERE bi.booking.id=:b_id", BookingItemHotel.class)
                    .setParameter("b_id", booking.getId())
                    .getSingleResultOrNull();

            BookingItemThingsToDo itemThingsToDo = session.createQuery("FROM BookingItemThingsToDo bi WHERE bi.booking.id=:b_id", BookingItemThingsToDo.class)
                    .setParameter("b_id", booking.getId())
                    .getSingleResultOrNull();

            BookingItemService itemService = session.createQuery("FROM BookingItemService bi WHERE bi.booking.id=:b_id", BookingItemService.class)
                    .setParameter("b_id", booking.getId())
                    .getSingleResultOrNull();

            if (itemHotel != null){
                map.put("categoryId", 1);
                map.put("categoryName", "Hotels");
                map.put("adultCount", itemHotel.getAdult_count());
                map.put("childCount", itemHotel.getChildren_count());
                map.put("roomCount", itemHotel.getRoom_count());
                map.put("nights", itemHotel.getNight_count());
                map.put("roomTypeId", itemHotel.getRoomTypes().getId());
                map.put("roomType", itemHotel.getRoomTypes().getType());

            } else if (itemThingsToDo != null) {
                map.put("categoryId", 2);
                map.put("categoryName", "Things To Do");
                map.put("adultCount", itemThingsToDo.getGuest_count());
                map.put("childCount", 0);

            } else if (itemService != null) {
                map.put("categoryId", 3);
                map.put("categoryName", "Services");
                map.put("adultCount", itemService.getGuest_count());
                map.put("childCount", 0);

            } else {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            dto.add(map);
        }

        session.close();

        return Response.ok(dto).build();
    }
}
