package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "booking_item_hotel")
public class BookingItemHotel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "adult_count", nullable = false)
    private int adult_count;

    @Column(name = "children_count", nullable = false)
    private int children_count;

    @Column(name = "room_count", nullable = false)
    private int room_count;

    @Column(name = "night_count", nullable = false)
    private int night_count;

    @ManyToOne
    @JoinColumn(name = "room_types_id")
    private RoomTypes roomTypes;

    @ManyToOne
    @JoinColumn(name = "bookings_id")
    private Booking booking;

    public BookingItemHotel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public RoomTypes getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(RoomTypes roomTypes) {
        this.roomTypes = roomTypes;
    }

    public int getNight_count() {
        return night_count;
    }

    public void setNight_count(int night_count) {
        this.night_count = night_count;
    }

    public int getRoom_count() {
        return room_count;
    }

    public void setRoom_count(int room_count) {
        this.room_count = room_count;
    }

    public int getChildren_count() {
        return children_count;
    }

    public void setChildren_count(int children_count) {
        this.children_count = children_count;
    }

    public int getAdult_count() {
        return adult_count;
    }

    public void setAdult_count(int adult_count) {
        this.adult_count = adult_count;
    }
}
