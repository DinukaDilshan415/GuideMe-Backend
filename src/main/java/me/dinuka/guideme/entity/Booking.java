package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "booking_id", length = 45, nullable = false)
    private String booking_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "booking_fee", nullable = false)
    private double booking_fee;

    @Column(name = "date_in", nullable = false)
    private Date date_in;

    @Column(name = "date_out", nullable = false)
    private Date date_out;

    @Column(name = "created_at", nullable = false)
    private Date created_at;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;


    public Booking() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBooking_fee() {
        return booking_fee;
    }

    public void setBooking_fee(double booking_fee) {
        this.booking_fee = booking_fee;
    }

    public Date getDate_in() {
        return date_in;
    }

    public void setDate_in(Date date_in) {
        this.date_in = date_in;
    }

    public Date getDate_out() {
        return date_out;
    }

    public void setDate_out(Date date_out) {
        this.date_out = date_out;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
