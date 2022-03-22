package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pms_client_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PMSClientMaster extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="client_code")
    private String clientCode;

    @Column(name="client_name")
    private String clientName;

    @Column(name="client_id")
    private Integer clientId;

    @Column(name="account_open_date")
    private Date accountOpenDate;

    @Transient
    private String intermediaryNameDistributor;

    @Transient
    private String schemeName;

    @Transient
    private String relmgrName;

    @Column(name="slab")
    private String slab;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="aif_id")
    private AIFClientMaster aifClientMaster;

    @Column(name="pan_number")
    private String panNumber;

    @ManyToOne
    @JoinColumn(name="sub_rm_id")
    private SubRM subRM;

    @ManyToOne
    @JoinColumn(name="invest_id")
    private InvestmentMaster investmentMaster;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

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

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public String getIntermediaryNameDistributor() {
        return intermediaryNameDistributor;
    }

    public void setIntermediaryNameDistributor(String intermediaryNameDistributor) {
        this.intermediaryNameDistributor = intermediaryNameDistributor;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getRelmgrName() {
        return relmgrName;
    }

    public void setRelmgrName(String relmgrName) {
        this.relmgrName = relmgrName;
    }

    public String getSlab() {
        return slab;
    }

    public void setSlab(String slab) {
        this.slab = slab;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public AIFClientMaster getAifClientMaster() {
        return aifClientMaster;
    }

    public void setAifClientMaster(AIFClientMaster aifClientMaster) {
        this.aifClientMaster = aifClientMaster;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public SubRM getSubRM() {
        return subRM;
    }

    public void setSubRM(SubRM subRM) {
        this.subRM = subRM;
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
}
