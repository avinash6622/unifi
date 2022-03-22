package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bcad_dist_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BCADDistributorFee extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @Column(name="opening_bal_upf_opt1")
    private Float openingBalOption1;

    @Column(name="upfront_paid_opt1")
    private Float upfrontPaidOption1;

    @Column(name="dist_share_opt1")
    private Float distShareOption1;

    @Column(name="closing_upf_opt1")
    private Float closingBalOption1;

    @Column(name="opening_bal_opt2")
    private Float openingBalOption2;

    @Column(name="trail_share_opt2")
    private Float trailShareOption2;

    @Column(name="trail_paid_opt2")
    private Float trailPaidOption2;

    @Column(name="trail_payable_opt2")
    private Float trailPayableOption2;

    @Column(name="start_date")
    private Date startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public Float getOpeningBalOption1() {
        return openingBalOption1;
    }

    public void setOpeningBalOption1(Float openingBalOption1) {
        this.openingBalOption1 = openingBalOption1;
    }

    public Float getUpfrontPaidOption1() {
        return upfrontPaidOption1;
    }

    public void setUpfrontPaidOption1(Float upfrontPaidOption1) {
        this.upfrontPaidOption1 = upfrontPaidOption1;
    }

    public Float getDistShareOption1() {
        return distShareOption1;
    }

    public void setDistShareOption1(Float distShareOption1) {
        this.distShareOption1 = distShareOption1;
    }

    public Float getClosingBalOption1() {
        return closingBalOption1;
    }

    public void setClosingBalOption1(Float closingBalOption1) {
        this.closingBalOption1 = closingBalOption1;
    }

    public Float getOpeningBalOption2() {
        return openingBalOption2;
    }

    public void setOpeningBalOption2(Float openingBalOption2) {
        this.openingBalOption2 = openingBalOption2;
    }

    public Float getTrailShareOption2() {
        return trailShareOption2;
    }

    public void setTrailShareOption2(Float trailShareOption2) {
        this.trailShareOption2 = trailShareOption2;
    }

    public Float getTrailPaidOption2() {
        return trailPaidOption2;
    }

    public void setTrailPaidOption2(Float trailPaidOption2) {
        this.trailPaidOption2 = trailPaidOption2;
    }

    public Float getTrailPayableOption2() {
        return trailPayableOption2;
    }

    public void setTrailPayableOption2(Float trailPayableOption2) {
        this.trailPayableOption2 = trailPayableOption2;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
