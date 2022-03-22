package com.bcad.application.domain;

public class NewCumulativeFee {
    private String feeType;
    private String narration;
    private Double otherIncome;
    private Double pmsUpfront;
    private Double pmsTrail;
    private Double pmsProfit;
    private Double aifHYF;
    private Double aifGreen;
    private Double aifBlendUpfront;
    private Double aifBlendTrail;

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(Double otherIncome) {
        this.otherIncome = otherIncome;
    }

    public Double getPmsUpfront() {
        return pmsUpfront;
    }

    public void setPmsUpfront(Double pmsUpfront) {
        this.pmsUpfront = pmsUpfront;
    }

    public Double getPmsTrail() {
        return pmsTrail;
    }

    public void setPmsTrail(Double pmsTrail) {
        this.pmsTrail = pmsTrail;
    }

    public Double getPmsProfit() {
        return pmsProfit;
    }

    public void setPmsProfit(Double pmsProfit) {
        this.pmsProfit = pmsProfit;
    }

    public Double getAifHYF() {
        return aifHYF;
    }

    public void setAifHYF(Double aifHYF) {
        this.aifHYF = aifHYF;
    }

    public Double getAifGreen() {
        return aifGreen;
    }

    public void setAifGreen(Double aifGreen) {
        this.aifGreen = aifGreen;
    }

    public Double getAifBlendUpfront() {
        return aifBlendUpfront;
    }

    public void setAifBlendUpfront(Double aifBlendUpfront) {
        this.aifBlendUpfront = aifBlendUpfront;
    }

    public Double getAifBlendTrail() {
        return aifBlendTrail;
    }

    public void setAifBlendTrail(Double aifBlendTrail) {
        this.aifBlendTrail = aifBlendTrail;
    }
}
