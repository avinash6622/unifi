package com.bcad.application.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "maker_capital_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MakerCapitalTransaction extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pms_client_id")
    private PMSClientMaster pmsClientMaster;

    @Column(name="client_code_scheme")
    private String clientCodeScheme;

    @Column(name="trans_date")
    private Date transDate;

    @Column(name="trans_desc")
    private String transDesc;

    @Column(name="credit_amt")
    private Double creditAmount;

    @Column(name="debit_amt")
    private Double debitAmount;

    @ManyToOne
    @JoinColumn(name="investment_id")
    private InvestmentMaster investmentMaster;

    @ManyToOne
    @JoinColumn(name="file_upload_id")
    private FileUpload fileUpload;

    @Column(name="int_deleted")
    private Integer isDeleted;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

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

    public String getClientCodeScheme() {
        return clientCodeScheme;
    }

    public void setClientCodeScheme(String clientCodeScheme) {
        this.clientCodeScheme = clientCodeScheme;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public InvestmentMaster getInvestmentMaster() {
        return investmentMaster;
    }

    public void setInvestmentMaster(InvestmentMaster investmentMaster) {
        this.investmentMaster = investmentMaster;
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
}
