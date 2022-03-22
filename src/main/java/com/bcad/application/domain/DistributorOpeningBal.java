package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dist_open_bal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DistributorOpeningBal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @Column(name="opening_bal")
    private Float openingBal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributorMaster getDistributoMaster() {
        return distributorMaster;
    }

    public void setDistributoMaster(DistributorMaster distributoMaster) {
        this.distributorMaster = distributoMaster;
    }

    public Float getOpeningBal() {
        return openingBal;
    }

    public void setOpeningBal(Float openingBal) {
        this.openingBal = openingBal;
    }
}
