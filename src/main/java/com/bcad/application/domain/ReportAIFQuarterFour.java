package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "report_aif_q4")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class  ReportAIFQuarterFour extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private AIFClientMaster aifClientMaster;

    @ManyToOne
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private SeriesMaster seriesMaster;

    @Column(name = "mnth_yr")
    private String mnthYear;

    @Column(name = "manage_fee")
    private Float manageFee;

    @Column(name = "perform_fee")
    private Float performFee;

    @Column(name = "tot_series_units")
    private Float totSeriesUnits;

    @Column(name = "tot_ind_units")
    private Float totIndUnits;

    @Column(name = "tot_clnt_fee")
    private Float totClientFee;

    @Column(name = "comm_perc")
    private Float commPerc;

    @Column(name = "comm_due")
    private Float commDue;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Integer endDate;

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

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public String getMnthYear() {
        return mnthYear;
    }

    public void setMnthYear(String mnthYear) {
        this.mnthYear = mnthYear;
    }

    public Float getManageFee() {
        return manageFee;
    }

    public void setManageFee(Float manageFee) {
        this.manageFee = manageFee;
    }

    public Float getPerformFee() {
        return performFee;
    }

    public void setPerformFee(Float performFee) {
        this.performFee = performFee;
    }

    public Float getTotSeriesUnits() {
        return totSeriesUnits;
    }

    public void setTotSeriesUnits(Float totSeriesUnits) {
        this.totSeriesUnits = totSeriesUnits;
    }

    public Float getTotIndUnits() {
        return totIndUnits;
    }

    public void setTotIndUnits(Float totIndUnits) {
        this.totIndUnits = totIndUnits;
    }

    public Float getTotClientFee() {
        return totClientFee;
    }

    public void setTotClientFee(Float totClientFee) {
        this.totClientFee = totClientFee;
    }

    public Float getCommPerc() {
        return commPerc;
    }

    public void setCommPerc(Float commPerc) {
        this.commPerc = commPerc;
    }

    public Float getCommDue() {
        return commDue;
    }

    public void setCommDue(Float commDue) {
        this.commDue = commDue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    
}
