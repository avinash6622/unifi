package com.bcad.application.domain;

import java.io.Serializable;
import java.util.Objects;

public class UpfrontLogBean implements Serializable {

    private Integer clientCode;
    private String distName;
    private String investName;
    private String sStatus;
    public Integer getClientCode() {
        return clientCode;
    }
    public void setClientCode(Integer clientCode2) {
        this.clientCode = clientCode2;
    }
    public String getDistName() {
        return distName;
    }
    public void setDistName(String distName) {
        this.distName = distName;
    }
    public String getInvestName() {
        return investName;
    }
    public void setInvestName(String investName) {
        this.investName = investName;
    }
    public String getsStatus() {
        return sStatus;
    }
    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpfrontLogBean that = (UpfrontLogBean) o;
        return clientCode.equals(that.clientCode) &&
            investName.equals(that.investName) &&
            sStatus.equals(that.sStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientCode, investName, sStatus);
    }

    @Override
    public String toString() {
        return "UpfrontLogBean{" +
            "clientCode=" + clientCode +
            ", distName='" + distName + '\'' +
            ", investName='" + investName + '\'' +
            ", sStatus='" + sStatus + '\'' +
            '}';
    }
}
