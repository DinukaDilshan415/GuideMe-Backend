package me.dinuka.guideme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", length = 45, nullable = false)
    private String city;

    @Column(name = "postal_code", length = 45, nullable = false)
    private String postal_code;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public UserAddress() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
