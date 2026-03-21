package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reviews")
public class Reviews {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "review_text", nullable = false)
    private String review_text;

    @Column(name = "created_at", nullable = false)
    private Date created_at;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public Reviews() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
