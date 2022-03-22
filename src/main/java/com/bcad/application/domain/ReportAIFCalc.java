package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "report_aif_calc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class  ReportAIFCalc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "client_name")
    private String clientName;

    @ManyToOne
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @Column(name = "series_name")
    private String seriesName;

    @Column(name = "aif_comm")
    private Float aifComm;

    @Column(name = "manage_fee")
    private Float manageFee;

    @Column(name = "perform_fee")
    private Float performFee;

    @Column(name = "tot_sum_of_series_units")
    private Float totSumOfSeriesUnits;
    
    @Column(name = "tot_sum_of_indiv_units")
    private Float totSumOfIndivUnits;

    @Column(name = "mng_fee_charged")
    private Float mngFeeCharged;

    @Column(name = "comm_due")
    private Float commDue;

    @ManyToOne
    @JoinColumn(name="report_id")
    private ReportGeneration reportGeneration;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Float getAifComm() {
        return aifComm;
    }

    public void setAifComm(Float aifComm) {
        this.aifComm = aifComm;
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

    public Float getTotSumOfSeriesUnits() {
        return totSumOfSeriesUnits;
    }

    public void setTotSumOfSeriesUnits(Float totSumOfSeriesUnits) {
        this.totSumOfSeriesUnits = totSumOfSeriesUnits;
    }

    public Float getTotSumOfIndivUnits() {
        return totSumOfIndivUnits;
    }

    public void setTotSumOfIndivUnits(Float totSumOfIndivUnits) {
        this.totSumOfIndivUnits = totSumOfIndivUnits;
    }

    public Float getMngFeeCharged() {
        return mngFeeCharged;
    }

    public void setMngFeeCharged(Float mngFeeCharged) {
        this.mngFeeCharged = mngFeeCharged;
    }

    public Float getCommDue() {
        return commDue;
    }

    public void setCommDue(Float commDue) {
        this.commDue = commDue;
    }

    public ReportGeneration getReportGeneration() {
        return reportGeneration;
    }

    public void setReportGeneration(ReportGeneration reportGeneration) {
        this.reportGeneration = reportGeneration;
    }
}
