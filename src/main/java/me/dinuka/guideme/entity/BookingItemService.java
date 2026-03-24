package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booking_item_service")
public class BookingItemService {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "guest_count", nullable = false)
    private int guest_count;

    @ManyToOne
    @JoinColumn(name = "bookings_id")
    private Booking booking;

    public BookingItemService() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuest_count() {
        return guest_count;
    }

    public void setGuest_count(int guest_count) {
        this.guest_count = guest_count;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
