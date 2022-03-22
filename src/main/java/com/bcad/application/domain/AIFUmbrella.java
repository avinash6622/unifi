package com.bcad.application.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aif_umbrella_monthly_calc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIFUmbrella {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="aif2_client_id")
    private ClientManagement clientManagement;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="sub_rm_id")
    private SubRM subRM;

    @ManyToOne
    @JoinColumn(name="aif2_series_id")
    private AIF2SeriesMaster aif2SeriesMaster;

    @Column(name="tot_of_units")
    private Double totOfUnits;

    @Column(name="unit_price")
    private Float unitPrice;

    @Column(name="no_of_units")
    private Float noOfUnits;

    @Column(name="from_date")
    private Date fromDate;

    @Column(name="management_fee")
    private Float managementFee;

    @Column(name="dist_share")
    private Float distShare;

    @Column(name="additiional_corpus")
    private Double additionalCorpus;

    @Column(name="opening_corpus")
    private Double openingCorpus;

    @Column(name="Upfront")
    private Double upFront;



    @Column(name="withdrawal")
    private String withdrawal;

    @Column(name="cumulative_corpus")
    private Double cumulativeCorpus;

    @Column(name="carry_forward")
    private Double carryForward;

    @Column(name="total_fee")
    private Double totalFee;

    @Column(name="adjustment")
    private Double adjustment;




    @Column(name="net_trail")
    private Double netTrail;

    @Column(name="upfront_adjus")
    private Double upfrontAdjus;

    @Column(name="carry_over")
    private Double carryOver;


    @Column(name ="performance_fee")
    private Double performanceFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientManagement getClientManagement() {
        return clientManagement;
    }

    public void setClientManagement(ClientManagement clientManagement) {
        this.clientManagement = clientManagement;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public SubRM getSubRM() {
        return subRM;
    }

    public void setSubRM(SubRM subRM) {
        this.subRM = subRM;
    }

    public AIF2SeriesMaster getAif2SeriesMaster() {
        return aif2SeriesMaster;
    }

    public void setAif2SeriesMaster(AIF2SeriesMaster aif2SeriesMaster) {
        this.aif2SeriesMaster = aif2SeriesMaster;
    }

    public Double getTotOfUnits() {
        return totOfUnits;
    }

    public void setTotOfUnits(Double totOfUnits) {
        this.totOfUnits = totOfUnits;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Float noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Float getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(Float managementFee) {
        this.managementFee = managementFee;
    }

    public Float getDistShare() {
        return distShare;
    }

    public void setDistShare(Float distShare) {
        this.distShare = distShare;
    }

    public Double getAdditionalCorpus() {
        return additionalCorpus;
    }

    public void setAdditionalCorpus(Double additionalCorpus) {
        this.additionalCorpus = additionalCorpus;
    }

    public Double getOpeningCorpus() {
        return openingCorpus;
    }

    public void setOpeningCorpus(Double openingCorpus) {
        this.openingCorpus = openingCorpus;
    }

    public Double getUpFront() {
        return upFront;
    }

    public void setUpFront(Double upFront) {
        this.upFront = upFront;
    }


    public String getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }

    public Double getCumulativeCorpus() {
        return cumulativeCorpus;
    }

    public void setCumulativeCorpus(Double cumulativeCorpus) {
        this.cumulativeCorpus = cumulativeCorpus;
    }

    public Double getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(Double carryForward) {
        this.carryForward = carryForward;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public Double getNetTrail() {
        return netTrail;
    }

    public void setNetTrail(Double netTrail) {
        this.netTrail = netTrail;
    }

    public Double getUpfrontAdjus() {
        return upfrontAdjus;
    }

    public void setUpfrontAdjus(Double upfrontAdjus) {
        this.upfrontAdjus = upfrontAdjus;
    }

    public Double getCarryOver() {
        return carryOver;
    }

    public void setCarryOver(Double carryOver) {
        this.carryOver = carryOver;
    }

    public Double getPerformanceFee() {
        return performanceFee;
    }

    public void setPerformanceFee(Double performanceFee) {
        this.performanceFee = performanceFee;
    }
}
