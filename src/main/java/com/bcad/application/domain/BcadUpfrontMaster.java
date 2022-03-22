package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bcad_upfront_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BcadUpfrontMaster extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_code")
    private String client_code;

    @ManyToOne
    @JoinColumn(name = "investment_id")
    private InvestmentMaster investmentMaster;

    @ManyToOne
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name = "rm_id")
    private RelationshipManager relationshipManager;

    @Column(name = "trans_date")
    private Date transDate;

    @Column(name = "initial_fund")
    private Float initialFund;

    @Column(name = "additional_fund")
    private Float additionalFund;

    @Column(name = "commission_amt")
    private Float commissionAmount;

    @Column(name = "capital_payout")
    private Float capitalPayout;

    @Column(name = "profit_payout")
    private Float profitPayout;

    @Column(name = "profit_renewed")
    private Float profitRenewed;

    @Column(name = "inter_ac_transfers")
    private Float interACTransfers;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "file_uploaded_id")
    private UploadMasterFiles fileUploadUpfront;

    @Column(name = "int_deleted")
    private Integer isDeleted;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientManagement ClientManagement;

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

    public String getClient_code() {
        return client_code;
    }

    public void setClient_code(String client_code) {
        this.client_code = client_code;
    }

    public InvestmentMaster getInvestmentMaster() {
        return investmentMaster;
    }

    public void setInvestmentMaster(InvestmentMaster investmentMaster) {
        this.investmentMaster = investmentMaster;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Float getInitialFund() {
        return initialFund;
    }

    public void setInitialFund(Float initialFund) {
        this.initialFund = initialFund;
    }

    public Float getAdditionalFund() {
        return additionalFund;
    }

    public void setAdditionalFund(Float additionalFund) {
        this.additionalFund = additionalFund;
    }

    public Float getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Float commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Float getCapitalPayout() {
        return capitalPayout;
    }

    public void setCapitalPayout(Float capitalPayout) {
        this.capitalPayout = capitalPayout;
    }

    public Float getProfitPayout() {
        return profitPayout;
    }

    public void setProfitPayout(Float profitPayout) {
        this.profitPayout = profitPayout;
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

    public UploadMasterFiles getFileUploadUpfront() {
        return fileUploadUpfront;
    }

    public void setFileUploadUpfront(UploadMasterFiles fileUploadUpfront) {
        this.fileUploadUpfront = fileUploadUpfront;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public com.bcad.application.domain.ClientManagement getClientManagement() {
        return ClientManagement;
    }

    public void setClientManagement(com.bcad.application.domain.ClientManagement clientManagement) {
        ClientManagement = clientManagement;
    }

    public Float getProfitRenewed() {
        return profitRenewed;
    }

    public void setProfitRenewed(Float profitRenewed) {
        this.profitRenewed = profitRenewed;
    }

    public Float getInterACTransfers() {
        return interACTransfers;
    }

    public void setInterACTransfers(Float interACTransfers) {
        this.interACTransfers = interACTransfers;
    }
}
