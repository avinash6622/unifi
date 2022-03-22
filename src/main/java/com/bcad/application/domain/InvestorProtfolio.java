package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "investor_portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvestorProtfolio extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private AIFClientMaster aifClientMaster;

    @ManyToOne
    @JoinColumn(name="series_id")
    private SeriesMaster seriesMaster;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @Column(name="mnth_yr")
    private String monthYr;

    @Column(name="cost_of_investment")
    private Float costInvestment;

    @Column(name="nav_per_unit_at_cost")
    private Float navUnits;

    @Column(name="no_of_units")
    private Float noOfUnits;

    @Column(name="nav_as_on")
    private Float navAsOn;

    @Column(name="portfolio_val")
    private Float protfolioValue;

    @Column(name="closing_units")
    private Float closingUnits;

    @Transient
    @JsonProperty
    private Float noOfRedemptingUnit=0f;

    @Transient
    @JsonProperty
    private Float costOfRedemptingUnit=0f;

    @Transient
    @JsonProperty
    private Date subscriptionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AIFClientMaster getAifClientMaster() {
        return aifClientMaster;
    }

    public void setAifClientMaster(AIFClientMaster aifClientMaster) {
        this.aifClientMaster = aifClientMaster;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public String getMonthYr() {
        return monthYr;
    }

    public void setMonthYr(String monthYr) {
        this.monthYr = monthYr;
    }

    public Float getCostInvestment() {
        return costInvestment;
    }

    public void setCostInvestment(Float costInvestment) {
        this.costInvestment = costInvestment;
    }

    public Float getNavUnits() {
        return navUnits;
    }

    public void setNavUnits(Float navUnits) {
        this.navUnits = navUnits;
    }

    public Float getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Float noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public Float getNavAsOn() {
        return navAsOn;
    }

    public void setNavAsOn(Float navAsOn) {
        this.navAsOn = navAsOn;
    }

    public Float getProtfolioValue() {
        return protfolioValue;
    }

    public void setProtfolioValue(Float protfolioValue) {
        this.protfolioValue = protfolioValue;
    }

    public Float getClosingUnits() {
        return closingUnits;
    }

    public void setClosingUnits(Float closingUnits) {
        this.closingUnits = closingUnits;
    }

    public Float getNoOfRedemptingUnit() {
        return noOfRedemptingUnit;
    }

    public void setNoOfRedemptingUnit(Float noOfRedemptingUnit) {
        this.noOfRedemptingUnit = noOfRedemptingUnit;
    }

    public Float getCostOfRedemptingUnit() {
        return costOfRedemptingUnit;
    }

    public void setCostOfRedemptingUnit(Float costOfRedemptingUnit) {
        this.costOfRedemptingUnit = costOfRedemptingUnit;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
