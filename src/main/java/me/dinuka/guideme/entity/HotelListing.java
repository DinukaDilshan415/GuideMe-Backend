package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hotel_listing")
public class HotelListing {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "per_room_count", nullable = false)
    private int per_room_count;

    @Column(name = "rooms_count", nullable = false)
    private int rooms_count;

    public HotelListing() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public int getPer_room_count() {
        return per_room_count;
    }

    public void setPer_room_count(int per_room_count) {
        this.per_room_count = per_room_count;
    }

    public int getRooms_count() {
        return rooms_count;
    }

    public void setRooms_count(int rooms_count) {
        this.rooms_count = rooms_count;
    }
}
