package me.dinuka.guideme.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "partner_plan_upgrade")
public class PartnerPlanUpgrade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "upgrade_date", nullable = false)
    private Date upgrade_date;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public PartnerPlanUpgrade() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpgrade_date() {
        return upgrade_date;
    }

    public void setUpgrade_date(Date upgrade_date) {
        this.upgrade_date = upgrade_date;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
