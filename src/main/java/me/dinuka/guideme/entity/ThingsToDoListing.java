package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "thingstodo_listing")
public class ThingsToDoListing {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "per_day_bookings", nullable = false)
    private int per_day_bookings;

    @Column(name = "guest_requerements", nullable = false)
    private String guest_requerements;

    @Column(name = "activity_level", nullable = false)
    private String activity_level;

    @Column(name = "what_to_bring", nullable = false)
    private String what_to_bring;

    public ThingsToDoListing() {
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

    public String getGuest_requerements() {
        return guest_requerements;
    }

    public void setGuest_requerements(String guest_requerements) {
        this.guest_requerements = guest_requerements;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public String getWhat_to_bring() {
        return what_to_bring;
    }

    public void setWhat_to_bring(String what_to_bring) {
        this.what_to_bring = what_to_bring;
    }
}
