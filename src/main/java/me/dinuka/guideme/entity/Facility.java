package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "facilities")
public class Facility implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 45)
    private String type;

    @Column(nullable = false, length = 45)
    private String icon;

    public Facility() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
