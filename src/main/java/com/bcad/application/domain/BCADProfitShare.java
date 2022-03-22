package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bcad_profit_share")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BCADProfitShare  extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private ClientManagement clientManagement;

    @Column(name="profit_share_income")
    private Float profitShareIncome;

    @Column(name="receipt_date")
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name="fileuploaded_id")
    private UploadMasterFiles fileUpload;

    @Column(name="int_deleted")
    private Integer isDeleted;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="investment_date")
    private Date investmentDate;
/*
    @Column(name="client_comm")
    private float clientcomm;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientManagement getClientManagement() {
        return clientManagement;
    }

    public void setClientManagement(ClientManagement clientManagement) {
        this.clientManagement = clientManagement;
    }

    public Float getProfitShareIncome() {
        return profitShareIncome;
    }

    public void setProfitShareIncome(Float profitShareIncome) {
        this.profitShareIncome = profitShareIncome;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public UploadMasterFiles getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadMasterFiles fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

   /* public float getClientcomm() {
        return clientcomm;
    }

    public void setClientcomm(float clientcomm) {
        this.clientcomm = clientcomm;
    }*/
}
