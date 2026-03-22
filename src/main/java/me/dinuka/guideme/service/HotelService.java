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

public class HotelService {
    public Response getMainHotels(String key) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<HotelListing> hotelListings = session.createQuery("FROM HotelListing h", HotelListing.class)
                .getResultList();

        if (hotelListings.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        List<HashMap<String, Object>> dto = new ArrayList<>();
        for (HotelListing hotel : hotelListings) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", hotel.getId());
            map.put("per_room_count", hotel.getPer_room_count());
            map.put("room_count", hotel.getRooms_count());
            map.put("title", hotel.getListing().getTitle());
            map.put("description", hotel.getListing().getDescription());
            map.put("address", hotel.getListing().getAddress());
            map.put("price", hotel.getListing().getPrice());
            map.put("latitude", hotel.getListing().getLatitude());
            map.put("longitude", hotel.getListing().getLongitude());
            map.put("location_name", hotel.getListing().getLocation_name());
            map.put("created_at", hotel.getListing().getCreated_at().toString());
            map.put("partner_name", hotel.getListing().getPartner().getName());
            map.put("partnerEmail", hotel.getListing().getPartner().getEmail());
            map.put("partnerMobile", hotel.getListing().getPartner().getMobile());
            map.put("partnerImageUrl", hotel.getListing().getPartner().getImage_url());
            map.put("partnerCountry", hotel.getListing().getPartner().getCountry());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date createdAt = hotel.getListing().getPartner().getCreated_at();

            map.put("partnerCreated_at", sdf.format(createdAt));

            List<ListingImages> listingImages = session.createQuery("FROM ListingImages li WHERE li.listing.id=:list_id ", ListingImages.class)
                    .setParameter("list_id", hotel.getListing().getId())
                    .getResultList();

            List<String> imageUris = new ArrayList<>();
            for (ListingImages listingImage : listingImages) {
                imageUris.add(listingImage.getUrl());
            }
            map.put("imageUris", imageUris);

            List<RoomTypes> roomTypes = session.createQuery("FROM RoomTypes rt WHERE rt.hotelListing.id=:list_id ", RoomTypes.class)
                    .setParameter("list_id", hotel.getId())
                    .getResultList();

            List<HashMap<String, Object>> roomTypesList = new ArrayList<>();
            for (RoomTypes roomType : roomTypes) {
                HashMap<String, Object> roomTypeMap = new HashMap<>();
                roomTypeMap.put("id", roomType.getId());
                roomTypeMap.put("type", roomType.getType());
                roomTypesList.add(roomTypeMap);
            }
            map.put("roomTypes", roomTypesList);

            List<HotelListingHasFacilities> facilities = session.createQuery("FROM HotelListingHasFacilities hhf WHERE hhf.hotel_listing.id=:list_id ", HotelListingHasFacilities.class)
                    .setParameter("list_id", hotel.getId())
                    .getResultList();

            List<FacilityDTO> facilityList = new ArrayList<>();
            for (HotelListingHasFacilities facility : facilities) {
                FacilityDTO facilityDTO = new FacilityDTO();
                facilityDTO.setId(facility.getFacility().getId());
                facilityDTO.setType(facility.getFacility().getType());
                facilityDTO.setIcon(facility.getFacility().getIcon());
                facilityList.add(facilityDTO);
            }
            map.put("facilities", facilityList);

            List<Reviews> reviews = session.createQuery("FROM Reviews r WHERE r.listing.id=:list_id ", Reviews.class)
                    .setParameter("list_id", hotel.getListing().getId())
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
