package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aif_redemption_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIFRedemptionMaster extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="redemption_date")
    private Date redemptionDate;

    @ManyToOne
    @JoinColumn(name="aif_client_id")
    private AIFClientMaster aifClientMaster;

    @ManyToOne
    @JoinColumn(name="series_id")
    private SeriesMaster seriesMaster;

    @Column(name="no_of_unit_hold")
    private Float noOfUnitHold;

    @Column(name="no_redemped_unit")
    private Float noRedempedUnit;

    @Column(name="closing_unit")
    private Float closingUnit;

    @Column(name="total_cost_of_unit")
    private Float totalCostOfUnit;

    @Column(name="total_cost_redemped")
    private Float totalCostRedemped;

    @Column(name="total_closing")
    private Float totalClosing;

    @Transient
    @JsonProperty
    private String FileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(Date redemptionDate) {
        this.redemptionDate = redemptionDate;
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

    public Float getNoOfUnitHold() {
        return noOfUnitHold;
    }

    public void setNoOfUnitHold(Float noOfUnitHold) {
        this.noOfUnitHold = noOfUnitHold;
    }

    public Float getNoRedempedUnit() {
        return noRedempedUnit;
    }

    public void setNoRedempedUnit(Float noRedempedUnit) {
        this.noRedempedUnit = noRedempedUnit;
    }

    public Float getClosingUnit() {
        return closingUnit;
    }

    public void setClosingUnit(Float closingUnit) {
        this.closingUnit = closingUnit;
    }

    public Float getTotalCostOfUnit() {
        return totalCostOfUnit;
    }

    public void setTotalCostOfUnit(Float totalCostOfUnit) {
        this.totalCostOfUnit = totalCostOfUnit;
    }

    public Float getTotalCostRedemped() {
        return totalCostRedemped;
    }

    public void setTotalCostRedemped(Float totalCostRedemped) {
        this.totalCostRedemped = totalCostRedemped;
    }

    public Float getTotalClosing() {
        return totalClosing;
    }

    public void setTotalClosing(Float totalClosing) {
        this.totalClosing = totalClosing;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
