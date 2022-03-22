package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "report_brok_pms")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class  ReportBrokeragePMS implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_code")
    private String clientCode;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @Column(name = "brok_fee")
    private Float brokeFee;

    @Column(name = "brok_total")
    private Float brokeTotal;

    @Column(name = "marketing_pay")
    private Float marketingPay;

    @Column(name = "brok_comm")
    private Float brokeComm;

    @Column(name = "pms_fee")
    private Float pmsFee;

    @Column(name = "pms_comm")
    private Float pmsComm;

    @Column(name = "advisory_pay")
    private Float advisoryPay;

    @Column(name = "trans_date")
    private String transDate;

    @Column(name = "initial_corpus")
    private Float initialCorpus;

    @Column(name = "add_corpus")
    private Float addCorpus;

    @Column(name = "total_corpus")
    private Float totalCorpus;

    @Column(name = "to_share_corpus")
    private Float toShareCorpus;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="report_id")
    private ReportGeneration reportGeneration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public Float getBrokeFee() {
        return brokeFee;
    }

    public void setBrokeFee(Float brokeFee) {
        this.brokeFee = brokeFee;
    }

    public Float getBrokeTotal() {
        return brokeTotal;
    }

    public void setBrokeTotal(Float brokeTotal) {
        this.brokeTotal = brokeTotal;
    }

    public Float getMarketingPay() {
        return marketingPay;
    }

    public void setMarketingPay(Float marketingPay) {
        this.marketingPay = marketingPay;
    }

    public Float getBrokeComm() {
        return brokeComm;
    }

    public void setBrokeComm(Float brokeComm) {
        this.brokeComm = brokeComm;
    }

    public Float getPmsFee() {
        return pmsFee;
    }

    public void setPmsFee(Float pmsFee) {
        this.pmsFee = pmsFee;
    }

    public Float getPmsComm() {
        return pmsComm;
    }

    public void setPmsComm(Float pmsComm) {
        this.pmsComm = pmsComm;
    }

    public Float getAdvisoryPay() {
        return advisoryPay;
    }

    public void setAdvisoryPay(Float advisoryPay) {
        this.advisoryPay = advisoryPay;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public Float getInitialCorpus() {
        return initialCorpus;
    }

    public void setInitialCorpus(Float initialCorpus) {
        this.initialCorpus = initialCorpus;
    }

    public Float getAddCorpus() {
        return addCorpus;
    }

    public void setAddCorpus(Float addCorpus) {
        this.addCorpus = addCorpus;
    }

    public Float getTotalCorpus() {
        return totalCorpus;
    }

    public void setTotalCorpus(Float totalCorpus) {
        this.totalCorpus = totalCorpus;
    }

    public Float getToShareCorpus() {
        return toShareCorpus;
    }

    public void setToShareCorpus(Float toShareCorpus) {
        this.toShareCorpus = toShareCorpus;
    }

    public ReportGeneration getReportGeneration() {
        return reportGeneration;
    }

    public void setReportGeneration(ReportGeneration reportGeneration) {
        this.reportGeneration = reportGeneration;
    }
}
