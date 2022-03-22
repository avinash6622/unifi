package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="bcad_monthly_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReportBcadMonthlyCalculation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private ClientManagement clientManagement;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="sub_rm_id")
    private SubRM subRM;

    @Column(name="from_date")
    private Date fromDate;

    @Column(name="opening_corpus")
    private Double openingCorpus;

    @Column(name="additional_corpus")
    private Double additionalCorpus;

    @Column(name="withdrawl_amt")
    private Double withdrawlAmount;

    @Column(name="cumulative_corpus")
    private Double cumulativeCorpus;

    @Column(name="carry_upfront_fee")
    private Double carryUpfront;

    @Column(name="upfront_fee")
    private Double upfrontFee;

    @Column(name="management_fee")
    private Double managementFee;

    @Column(name="performance_fee")
    private Double performanceFee;

    @Column(name="total_fee")
    private Double totalFee;

    @Column(name="distributor_share")
    private Double distributorShare;

    @Column(name="adj_upfront_fee")
    private Double adjUpfrontFee;

    @Column(name="net_trial_payable")
    private Double netTrialPayable;

    @Column(name="upfront_withdrawl")
    private Double upfrontWithdrawl;

    @Column(name="trial_upfront_payable")
    private Double trialUpfrontPayable;

    @Column(name="three_year")
    private String threeYear;

    @Column(name="minus_value")
    private Double minusValue;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Double getOpeningCorpus() {
        return openingCorpus;
    }

    public void setOpeningCorpus(Double openingCorpus) {
        this.openingCorpus = openingCorpus;
    }

    public Double getAdditionalCorpus() {
        return additionalCorpus;
    }

    public void setAdditionalCorpus(Double additionalCorpus) {
        this.additionalCorpus = additionalCorpus;
    }

    public Double getCarryUpfront() {
        return carryUpfront;
    }

    public void setCarryUpfront(Double carryUpfront) {
        this.carryUpfront = carryUpfront;
    }

    public Double getUpfrontFee() {
        return upfrontFee;
    }

    public void setUpfrontFee(Double upfrontFee) {
        this.upfrontFee = upfrontFee;
    }

    public Double getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(Double managementFee) {
        this.managementFee = managementFee;
    }

    public Double getPerformanceFee() {
        return performanceFee;
    }

    public void setPerformanceFee(Double performanceFee) {
        this.performanceFee = performanceFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getDistributorShare() {
        return distributorShare;
    }

    public void setDistributorShare(Double distributorShare) {
        this.distributorShare = distributorShare;
    }

    public Double getAdjUpfrontFee() {
        return adjUpfrontFee;
    }

    public void setAdjUpfrontFee(Double adjUpfrontFee) {
        this.adjUpfrontFee = adjUpfrontFee;
    }

    public Double getNetTrialPayable() {
        return netTrialPayable;
    }

    public void setNetTrialPayable(Double netTrialPayable) {
        this.netTrialPayable = netTrialPayable;
    }

    public Double getUpfrontWithdrawl() {
        return upfrontWithdrawl;
    }

    public void setUpfrontWithdrawl(Double upfrontWithdrawl) {
        this.upfrontWithdrawl = upfrontWithdrawl;
    }

    public Double getTrialUpfrontPayable() {
        return trialUpfrontPayable;
    }

    public void setTrialUpfrontPayable(Double trialUpfrontPayable) {
        this.trialUpfrontPayable = trialUpfrontPayable;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Double getCumulativeCorpus() {
        return cumulativeCorpus;
    }

    public void setCumulativeCorpus(Double cumulativeCorpus) {
        this.cumulativeCorpus = cumulativeCorpus;
    }

    public Double getWithdrawlAmount() {
        return withdrawlAmount;
    }

    public void setWithdrawlAmount(Double withdrawlAmount) {
        this.withdrawlAmount = withdrawlAmount;
    }

    public String getThreeYear() {
        return threeYear;
    }

    public void setThreeYear(String threeYear) {
        this.threeYear = threeYear;
    }

    public Double getMinusValue() {
        return minusValue;
    }

    public void setMinusValue(Double minusValue) {
        this.minusValue = minusValue;
    }

    @Override
    public String toString() {
        return "ReportBcadMonthlyCalculation{" +
            "id=" + id +
            ", clientManagement=" + clientManagement +
            ", product=" + product +
            ", distributorMaster=" + distributorMaster +
            ", relationshipManager=" + relationshipManager +
            ", subRM=" + subRM +
            ", fromDate=" + fromDate +
            ", openingCorpus=" + openingCorpus +
            ", additionalCorpus=" + additionalCorpus +
            ", withdrawlAmount=" + withdrawlAmount +
            ", cumulativeCorpus=" + cumulativeCorpus +
            ", carryUpfront=" + carryUpfront +
            ", upfrontFee=" + upfrontFee +
            ", managementFee=" + managementFee +
            ", performanceFee=" + performanceFee +
            ", totalFee=" + totalFee +
            ", distributorShare=" + distributorShare +
            ", adjUpfrontFee=" + adjUpfrontFee +
            ", netTrialPayable=" + netTrialPayable +
            ", upfrontWithdrawl=" + upfrontWithdrawl +
            ", trialUpfrontPayable=" + trialUpfrontPayable +
            '}';
    }
}
