package com.bcad.application.bean;

public class PMSStrategyBean {

    private String clientCode;
    private String clientName;
    private String clientCodeScheme;
    private Float pmsNavFee;
    private Float commissionValue;
    private Float distShare;

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

    public String getClientCodeScheme() {
        return clientCodeScheme;
    }

    public void setClientCodeScheme(String clientCodeScheme) {
        this.clientCodeScheme = clientCodeScheme;
    }

    public Float getPmsNavFee() {
        return pmsNavFee;
    }

    public void setPmsNavFee(Float pmsNavFee) {
        this.pmsNavFee = pmsNavFee;
    }

    public Float getCommissionValue() {
        return commissionValue;
    }

    public void setCommissionValue(Float commissionValue) {
        this.commissionValue = commissionValue;
    }

    public Float getDistShare() {
        return distShare;
    }

    public void setDistShare(Float distShare) {
        this.distShare = distShare;
    }
}
