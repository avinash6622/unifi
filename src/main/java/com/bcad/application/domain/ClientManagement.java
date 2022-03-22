package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="bcad_client_master_cd")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientManagement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="client_code")
    private String clientCode;

    @Column(name="client_name")
    private String clientName;

    @Column(name="pan_no")
    private String panNo;

    @ManyToOne
    @JoinColumn(name="dist_master_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="sub_rm_id")
    private SubRM subRM;

    @ManyToOne
    @JoinColumn(name="option_id")
    private DistributorOption distributorOption;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Transient
    private String status;
    @Transient
    private String code;

    @Column(name="account_open_date")
    private Date accountopendate;

    @Column(name="SLAB")
    private String slab;

    @Column(name="client_id")
    private Integer clientId;

   /* public ClientManagement() {
    }*/


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

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
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

    public SubRM getSubRM() {
        return subRM;
    }

    public void setSubRM(SubRM subRM) {
        this.subRM = subRM;
    }

    public DistributorOption getDistributorOption() {
        return distributorOption;
    }

    public void setDistributorOption(DistributorOption distributorOption) {
        this.distributorOption = distributorOption;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Date getAccountopendate() {
        return accountopendate;
    }

    public void setAccountopendate(Date accountopendate) {
        this.accountopendate = accountopendate;
    }

    public String getSlab() {
        return slab;
    }

    public void setSlab(String slab) {
        this.slab = slab;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "ClientManagement{" +
            "id=" + id +
            ", clientCode='" + clientCode + '\'' +
            ", clientName='" + clientName + '\'' +
            ", panNo='" + panNo + '\'' +
            ", distributorMaster=" + distributorMaster +
            ", relationshipManager=" + relationshipManager +
            ", subRM=" + subRM +
            ", distributorOption=" + distributorOption +
            ", product=" + product +
            ", status='" + status + '\'' +
            ", code='" + code + '\'' +
            ", accountopendate=" + accountopendate +
            ", slab='" + slab + '\'' +
            '}';
    }
}


