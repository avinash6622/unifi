package com.bcad.application.domain;

public class InvestorLogBean {

    private Integer clientCode;
    private String distName;
    private String seriesName;
    private String status;

    public Integer getClientCode() {
        return clientCode;
    }
    public void setClientCode(Integer clientCode) {
        this.clientCode = clientCode;
    }
    public String getDistName() {
        return distName;
    }
    public void setDistName(String distName) {
        this.distName = distName;
    }
    public String getSeriesName() {
        return seriesName;
    }
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
