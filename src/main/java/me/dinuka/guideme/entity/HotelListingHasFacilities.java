package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hotel_listing_has_facilities")
public class HotelListingHasFacilities {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "hotel_listing_id")
    private HotelListing hotel_listing;

    @ManyToOne
    @JoinColumn(name = "facilities_id")
    private Facility facility;

    public HotelListingHasFacilities() {
    }

    public HotelListing getHotel_listing() {
        return hotel_listing;
    }

    public void setHotel_listing(HotelListing hotel_listing) {
        this.hotel_listing = hotel_listing;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
