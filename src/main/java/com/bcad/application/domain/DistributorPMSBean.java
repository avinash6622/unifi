package com.bcad.application.domain;

public class DistributorPMSBean {

    private String clientCode;
    private String clientName;
    private String clientCodeScheme;
   // private Float feeUCPL;
    private Float total;
    //private Float marketingPayable;
    private String pmsFee;
    private Float pmsNavFee;
    //private Float advisoryFee;
    private PMSClientMaster pmsClientMaster;
    private String transDate;
    private Float initialFund;
    private Float addFund;
    private Float totCorpus;
    private Float totShareCorpus;
    private ClientFeeCommission clientFeeCommission;
    private Float pmsDistShare;
    private String strategyName;

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

   /* public Float getFeeUCPL() {
        return feeUCPL;
    }

    public void setFeeUCPL(Float feeUCPL) {
        this.feeUCPL = feeUCPL;
    }*/

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    /*public Float getMarketingPayable() {
        return marketingPayable;
    }

    public void setMarketingPayable(Float marketingPayable) {
        this.marketingPayable = marketingPayable;
    }
*/
    public String getPmsFee() {
        return pmsFee;
    }

    public void setPmsFee(String pmsFee) {
        this.pmsFee = pmsFee;
    }

   /* public Float getAdvisoryFee() {
        return advisoryFee;
    }

    public void setAdvisoryFee(Float advisoryFee) {
        this.advisoryFee = advisoryFee;
    }*/

    public PMSClientMaster getPmsClientMaster() {
        return pmsClientMaster;
    }

    public void setPmsClientMaster(PMSClientMaster pmsClientMaster) {
        this.pmsClientMaster = pmsClientMaster;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public Float getInitialFund() {
        return initialFund;
    }

    public void setInitialFund(Float initialFund) {
        this.initialFund = initialFund;
    }

    public Float getAddFund() {
        return addFund;
    }

    public void setAddFund(Float addFund) {
        this.addFund = addFund;
    }

    public Float getTotCorpus() {
        return totCorpus;
    }

    public void setTotCorpus(Float totCorpus) {
        this.totCorpus = totCorpus;
    }

    public Float getTotShareCorpus() {
        return totShareCorpus;
    }

    public void setTotShareCorpus(Float totShareCorpus) {
        this.totShareCorpus = totShareCorpus;
    }

    public ClientFeeCommission getClientFeeCommission() {
        return clientFeeCommission;
    }

    public void setClientFeeCommission(ClientFeeCommission clientFeeCommission) {
        this.clientFeeCommission = clientFeeCommission;
    }

    public String getClientCodeScheme() {
        return clientCodeScheme;
    }

    public void setClientCodeScheme(String clientCodeScheme) {
        this.clientCodeScheme = clientCodeScheme;
    }

    public Float getPmsDistShare() {
        return pmsDistShare;
    }

    public void setPmsDistShare(Float pmsDistShare) {
        this.pmsDistShare = pmsDistShare;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;

    }

    public Float getPmsNavFee() {
        return pmsNavFee;
    }

    public void setPmsNavFee(Float pmsNavFee) {
        this.pmsNavFee = pmsNavFee;
    }
}
