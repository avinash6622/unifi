package com.bcad.application.domain;

import java.math.BigDecimal;

public class CumulativeDistributorReportBean {

    private BigDecimal currentTrailAmt;
    private BigDecimal currentUpfrontAmt;
    private BigDecimal currentTotalPayableAmt;
    private BigDecimal currentPaidAmount;
    private BigDecimal currentClosing;

    public BigDecimal getCurrentTrailAmt() {
        return currentTrailAmt;
    }

    public void setCurrentTrailAmt(BigDecimal currentTrailAmt) {
        this.currentTrailAmt = currentTrailAmt;
    }

    public BigDecimal getCurrentUpfrontAmt() {
        return currentUpfrontAmt;
    }

    public void setCurrentUpfrontAmt(BigDecimal currentUpfrontAmt) {
        this.currentUpfrontAmt = currentUpfrontAmt;
    }

    public BigDecimal getCurrentTotalPayableAmt() {
        return currentTotalPayableAmt;
    }

    public void setCurrentTotalPayableAmt(BigDecimal currentTotalPayableAmt) {
        this.currentTotalPayableAmt = currentTotalPayableAmt;
    }

    public BigDecimal getCurrentPaidAmount() {
        return currentPaidAmount;
    }

    public void setCurrentPaidAmount(BigDecimal currentPaidAmount) {
        this.currentPaidAmount = currentPaidAmount;
    }

    public BigDecimal getCurrentClosing() {
        return currentClosing;
    }

    public void setCurrentClosing(BigDecimal currentClosing) {
        this.currentClosing = currentClosing;
    }
}
