package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_listing")
public class ServiceListing {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "per_day_bookings", nullable = false)
    private int per_day_bookings;

    public ServiceListing() {
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

    public int getPer_day_bookings() {
        return per_day_bookings;
    }

    public void setPer_day_bookings(int per_day_bookings) {
        this.per_day_bookings = per_day_bookings;
    }
}
