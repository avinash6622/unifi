package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "maker_fee_pms_profit_share")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MakerProfitShare extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private PMSClientMaster pmsClientMaster;

    @ManyToOne
    @JoinColumn(name="investment_id")
    private InvestmentMaster investmentMaster;

    @Transient
    @JsonProperty
    private String productName;

    @Column(name="profit_share_income")
    private Float profitShareIncome;

    @Column(name="receipt_date")
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name="fileuploaded_id")
    private FileUpload fileUpload;

    @Column(name="int_deleted")
    private Integer isDeleted;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="investment_date")
    private Date investmentDate;

    /*@Column(name="client_comm")
    private float clientcomm;*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PMSClientMaster getPmsClientMaster() {
        return pmsClientMaster;
    }

    public void setPmsClientMaster(PMSClientMaster pmsClientMaster) {
        this.pmsClientMaster = pmsClientMaster;
    }

    public InvestmentMaster getInvestmentMaster() {
        return investmentMaster;
    }

    public void setInvestmentMaster(InvestmentMaster investmentMaster) {
        this.investmentMaster = investmentMaster;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
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

    /*public float getClientcomm() {
        return clientcomm;
    }

    public void setClientcomm(float clientcomm) {
        this.clientcomm = clientcomm;
    }*/
}
