package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "generic_aif_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIFDistributorFee extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="opening_bal_fee")
    private Float openingBalFee;

    @Column(name="upfront_fee")
    private Float upfrontFee;

    @Column(name="paid_amt")
    private Float paidAmt;

    @Column(name="dist_share")
    private Float distributorShare;

    @Column(name="closing_bal")
    private Float closingBal;

    @Column(name="payable_amt")
    private Float payableAmt;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getOpeningBalFee() {
        return openingBalFee;
    }

    public void setOpeningBalFee(Float openingBalFee) {
        this.openingBalFee = openingBalFee;
    }

    public Float getUpfrontFee() {
        return upfrontFee;
    }

    public void setUpfrontFee(Float upfrontFee) {
        this.upfrontFee = upfrontFee;
    }

    public Float getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Float paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Float getClosingBal() {
        return closingBal;
    }

    public void setClosingBal(Float closingBal) {
        this.closingBal = closingBal;
    }

    public Float getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(Float payableAmt) {
        this.payableAmt = payableAmt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Float getDistributorShare() {
        return distributorShare;
    }

    public void setDistributorShare(Float distributorShare) {
        this.distributorShare = distributorShare;
    }
}
