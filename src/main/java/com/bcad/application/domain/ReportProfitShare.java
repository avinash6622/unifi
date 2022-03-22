package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "report_profit_share")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReportProfitShare implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "profit_fee")
    private Float profitFee;

    @Column(name = "profit_comm")
    private Float profitComm;

    @Column(name = "fee_pay")
    private Float feePay;

    @Column(name = "receipt_date")
    private Date receiptDate;

    @Column(name = "strategy_name")
    private String strategyName;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "report_id")
    private ReportGeneration reportGeneration;

    @Transient
    @JsonProperty
    private ClientFeeCommission clientFeeCommission;

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

    public Float getProfitFee() {
        return profitFee;
    }

    public void setProfitFee(Float profitFee) {
        this.profitFee = profitFee;
    }

    public Float getProfitComm() {
        return profitComm;
    }

    public void setProfitComm(Float profitComm) {
        this.profitComm = profitComm;
    }

    public Float getFeePay() {
        return feePay;
    }

    public void setFeePay(Float feePay) {
        this.feePay = feePay;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public ReportGeneration getReportGeneration() {
        return reportGeneration;
    }

    public void setReportGeneration(ReportGeneration reportGeneration) {
        this.reportGeneration = reportGeneration;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public ClientFeeCommission getClientFeeCommission() {
        return clientFeeCommission;
    }

    public void setClientFeeCommission(ClientFeeCommission clientFeeCommission) {
        this.clientFeeCommission = clientFeeCommission;
    }
}
