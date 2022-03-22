package com.bcad.application.bean;

public class WSBean {

    private String clientCode;
    private String dateMonth;
    private Float pmsNav;
    private String comments;


    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth;
    }

    public Float getPmsNav() {
        return pmsNav;
    }

    public void setPmsNav(Float pmsNav) {
        this.pmsNav = pmsNav;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
