package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fee_trail_upfront_trans")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FeeTrailUpfrontTrans  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @Column(name="trail_amt")
    private Float trailAmount;

    @Column(name="upfront_amt")
    private Float upfrontAmount;

    @Column(name="payable_amt")
    private Float payableAmount;

    @Column(name="trail_opening_bal")
    private Float trailOpeningBalance;

    @Column(name="trail_closing_bal")
    private Float trailClosingBalance;

    @Column(name="upfront_opening_bal")
    private Float upfrontOpeningBalance;

    @Column(name="upfront_closing_bal")
    private Float upfrontClosingBalance;

    @Column(name="closing_amt")
    private Float closingAmount;

    @Column(name="paid_amt")
    private Float padiAmount;

    @Column(name="trans_date")
    private Date transDate;

    @Column(name="int_details_updated_flag")
    private Integer detailsUpdatedFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributoMaster(DistributorMaster distributoMaster) {
        this.distributorMaster = distributoMaster;
    }

    public Float getTrailAmount() {
        return trailAmount;
    }

    public void setTrailAmount(Float trailAmount) {
        this.trailAmount = trailAmount;
    }

    public Float getUpfrontAmount() {
        return upfrontAmount;
    }

    public void setUpfrontAmount(Float upfrontAmount) {
        this.upfrontAmount = upfrontAmount;
    }

    public Float getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Float payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Float getTrailOpeningBalance() {
        return trailOpeningBalance;
    }

    public void setTrailOpeningBalance(Float trailOpeningBalance) {
        this.trailOpeningBalance = trailOpeningBalance;
    }

    public Float getTrailClosingBalance() {
        return trailClosingBalance;
    }

    public void setTrailClosingBalance(Float trailClosingBalance) {
        this.trailClosingBalance = trailClosingBalance;
    }

    public Float getUpfrontOpeningBalance() {
        return upfrontOpeningBalance;
    }

    public void setUpfrontOpeningBalance(Float upfrontOpeningBalance) {
        this.upfrontOpeningBalance = upfrontOpeningBalance;
    }

    public Float getUpfrontClosingBalance() {
        return upfrontClosingBalance;
    }

    public void setUpfrontClosingBalance(Float upfrontClosingBalance) {
        this.upfrontClosingBalance = upfrontClosingBalance;
    }

    public Float getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Float closingAmount) {
        this.closingAmount = closingAmount;
    }

    public Float getPadiAmount() {
        return padiAmount;
    }

    public void setPadiAmount(Float padiAmount) {
        this.padiAmount = padiAmount;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Integer getDetailsUpdatedFlag() {
        return detailsUpdatedFlag;
    }

    public void setDetailsUpdatedFlag(Integer detailsUpdatedFlag) {
        this.detailsUpdatedFlag = detailsUpdatedFlag;
    }
}
