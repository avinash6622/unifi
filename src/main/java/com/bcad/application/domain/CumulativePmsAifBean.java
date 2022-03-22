package com.bcad.application.domain;

public class CumulativePmsAifBean {

    private String brokerageName;
    private Float brokerageTotal;
    private String pmsName;
    private Float pmsTotal;
    private String profitName;
    private Float profitTotal;
    private String aifName;
    private String aifTotal;
    public String getPmsName() {
        return pmsName;
    }
    public void setPmsName(String pmsName) {
        this.pmsName = pmsName;
    }
    public Float getPmsTotal() {
        return pmsTotal;
    }
    public void setPmsTotal(Float pmsTotal) {
        this.pmsTotal = pmsTotal;
    }
    public String getAifName() {
        return aifName;
    }
    public void setAifName(String aifName) {
        this.aifName = aifName;
    }
    public String getAifTotal() {
        return aifTotal;
    }
    public void setAifTotal(String aifTotal) {
        this.aifTotal = aifTotal;
    }
    public String getBrokerageName() {
        return brokerageName;
    }
    public void setBrokerageName(String brokerageName) {
        this.brokerageName = brokerageName;
    }
    public Float getBrokerageTotal() {
        return brokerageTotal;
    }
    public void setBrokerageTotal(Float brokerageTotal) {
        this.brokerageTotal = brokerageTotal;
    }
    public String getProfitName() {
        return profitName;
    }
    public void setProfitName(String profitName) {
        this.profitName = profitName;
    }
    public Float getProfitTotal() {
        return profitTotal;
    }
    public void setProfitTotal(Float profitTotal) {
        this.profitTotal = profitTotal;
    }



}
