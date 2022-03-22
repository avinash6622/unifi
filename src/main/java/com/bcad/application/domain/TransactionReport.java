package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ws_client_code")
    private Integer wsClientCode;

    @Column(name="ws_account_code")
    private String wsAccountCode;

    @Column(name="client_name")
    private String clientName;

    @Column(name="tran_date")
    private Date tranDate;

    @Column(name="security_code")
    private String securityCode;

    @Column(name="security_name")
    private String securityName;

    @Column(name="net_amount")
    private Double netAmount;

    @Column(name ="series")
    private String series;


    @Column(name="store_file_location")
    private String fileLocation;

    @Column(name="int_deleted")
    private Integer isDeleted = 0;


    @Column(name ="quantity")
    private Float quantity;


    @Column(name ="rate")
    private Float rate;

    @Column(name="series_id")
    private Long seriesId;



    @Transient
    private String status;
    @Transient
    private String code;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWsClientCode() {
        return wsClientCode;
    }

    public void setWsClientCode(Integer wsClientCode) {
        this.wsClientCode = wsClientCode;
    }

    public String getWsAccountCode() {
        return wsAccountCode;
    }

    public void setWsAccountCode(String wsAccountCode) {
        this.wsAccountCode = wsAccountCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }


    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }


    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }
}
