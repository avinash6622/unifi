package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aif2_monthly_calc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIF2MonthlyCalculation extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="aif2_client_id")
    private ClientManagement clientManagement;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="sub_rm_id")
    private SubRM subRM;

    @ManyToOne
    @JoinColumn(name="aif2_series_id")
    private AIF2SeriesMaster aif2SeriesMaster;

    @Column(name="tot_of_units")
    private Float totOfUnits;

    @Column(name="unit_price")
    private Float unitPrice;

    @Column(name="no_of_units")
    private Float noOfUnits;

    @Column(name="from_date")
    private Date fromDate;

    @Column(name="management_fee")
    private Float managementFee;

    @Column(name="dist_share")
    private Float distShare;

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

    public Float getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Float noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public Float getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(Float managementFee) {
        this.managementFee = managementFee;
    }

    public Float getDistShare() {
        return distShare;
    }

    public void setDistShare(Float distShare) {
        this.distShare = distShare;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public AIF2SeriesMaster getAif2SeriesMaster() {
        return aif2SeriesMaster;
    }

    public void setAif2SeriesMaster(AIF2SeriesMaster aif2SeriesMaster) {
        this.aif2SeriesMaster = aif2SeriesMaster;
    }

    public Float getTotOfUnits() {
        return totOfUnits;
    }

    public void setTotOfUnits(Float totOfUnits) {
        this.totOfUnits = totOfUnits;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }
}
