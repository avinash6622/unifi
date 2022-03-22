package com.bcad.application.domain;

public class ReportAifDistBean {

    private String clientCode;
    private String clientName;
    private String seriesName;
    private Float distComm;
    private Float manageFee;
    private Float PerformFee;
    private Float sCommission;
    private Float sumOfUnitSeries;
    private Float totNoOfUnits;
    private Float manageFeeCharged;
    private SeriesMaster seriesMaster;


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
    public String getSeriesName() {
        return seriesName;
    }
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }
    public Float getManageFee() {
        return manageFee;
    }
    public void setManageFee(Float manageFee) {
        this.manageFee = manageFee;
    }
    public Float getPerformFee() {
        return PerformFee;
    }
    public void setPerformFee(Float performFee) {
        PerformFee = performFee;
    }
    public Float getsCommission() {
        return sCommission;
    }
    public void setsCommission(Float sCommission) {
        this.sCommission = sCommission;
    }
    public Float getDistComm() {
        return distComm;
    }
    public void setDistComm(Float distComm) {
        this.distComm = distComm;
    }
    public Float getSumOfUnitSeries() {
        return sumOfUnitSeries;
    }
    public void setSumOfUnitSeries(Float sumOfUnitSeries) {
        this.sumOfUnitSeries = sumOfUnitSeries;
    }
    public Float getTotNoOfUnits() {
        return totNoOfUnits;
    }
    public void setTotNoOfUnits(Float totNoOfUnits) {
        this.totNoOfUnits = totNoOfUnits;
    }
    public Float getManageFeeCharged() {
        return manageFeeCharged;
    }
    public void setManageFeeCharged(Float manageFeeCharged) {
        this.manageFeeCharged = manageFeeCharged;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }
}
