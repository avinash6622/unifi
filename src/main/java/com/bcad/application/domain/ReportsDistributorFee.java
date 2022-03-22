package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reports_dist_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class  ReportsDistributorFee extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @Column(name = "marketing_fee")
    private Float marketingFee;

    @Column(name = "advisory_fee")
    private Float advisoryFee;

    @Column(name = "profit_share_fee")
    private Float profitShareFee;

    @Column(name = "pms_total")
    private Float pmsTotal;

    @Column(name = "aif_manag_total")
    private Float aifManagTotal;

    @Column(name = "aif_perf_total")
    private Float aifPerfTotal;

    @Column(name = "aif_total")
    private Float aifTotal;

    @Column(name = "upfront_amount")
    private Float upfrontAmount;

    @Column(name = "opening_bal")
    private Float openingBal;

    @Column(name = "closing_bal")
    private Float closingBal;

    @Column(name = "payable_amount")
    private Float payableAmount;

    @Column(name = "paid_amount")
    private Float paidAmount;

    @Column(name = "int_details_updated_flag")
    private Integer detailsUpdatedFlag;

    @Column(name = "start_date")
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

    public Float getMarketingFee() {
        return marketingFee;
    }

    public void setMarketingFee(Float marketingFee) {
        this.marketingFee = marketingFee;
    }

    public Float getAdvisoryFee() {
        return advisoryFee;
    }

    public void setAdvisoryFee(Float advisoryFee) {
        this.advisoryFee = advisoryFee;
    }

    public Float getProfitShareFee() {
        return profitShareFee;
    }

    public void setProfitShareFee(Float profitShareFee) {
        this.profitShareFee = profitShareFee;
    }

    public Float getPmsTotal() {
        return pmsTotal;
    }

    public void setPmsTotal(Float pmsTotal) {
        this.pmsTotal = pmsTotal;
    }

    public Float getAifManagTotal() {
        return aifManagTotal;
    }

    public void setAifManagTotal(Float aifManagTotal) {
        this.aifManagTotal = aifManagTotal;
    }

    public Float getAifPerfTotal() {
        return aifPerfTotal;
    }

    public void setAifPerfTotal(Float aifPerfTotal) {
        this.aifPerfTotal = aifPerfTotal;
    }

    public Float getAifTotal() {
        return aifTotal;
    }

    public void setAifTotal(Float aifTotal) {
        this.aifTotal = aifTotal;
    }

    public Float getUpfrontAmount() {
        return upfrontAmount;
    }

    public void setUpfrontAmount(Float upfrontAmount) {
        this.upfrontAmount = upfrontAmount;
    }

    public Float getOpeningBal() {
        return openingBal;
    }

    public void setOpeningBal(Float openingBal) {
        this.openingBal = openingBal;
    }

    public Float getClosingBal() {
        return closingBal;
    }

    public void setClosingBal(Float closingBal) {
        this.closingBal = closingBal;
    }

    public Float getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Float payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getDetailsUpdatedFlag() {
        return detailsUpdatedFlag;
    }

    public void setDetailsUpdatedFlag(Integer detailsUpdatedFlag) {
        this.detailsUpdatedFlag = detailsUpdatedFlag;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
