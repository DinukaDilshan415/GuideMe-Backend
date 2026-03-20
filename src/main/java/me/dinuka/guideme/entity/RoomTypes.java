package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_types")
public class RoomTypes {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "hotel_listing_id")
    private HotelListing hotelListing;

    public RoomTypes() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HotelListing getHotelListing() {
        return hotelListing;
    }

    public void setHotelListing(HotelListing hotelListing) {
        this.hotelListing = hotelListing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
