package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "status")
public class Status implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    public Status() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
