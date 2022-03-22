package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "report_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class  ReportGeneration extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date toDate;

    @Transient
    @JsonProperty
    List<RelationshipManager> relationshipManager;

    @Transient
    @JsonProperty
    List<DistributorMaster> distributorMaster;

    @Transient
    @JsonProperty
    RelationshipManager relationManage;

    @Column(name = "details_updated_flag")
    private Integer detailsUpdatedFlag;

    @ManyToOne
    @JoinColumn(name = "dist_id")
    private DistributorMaster distributorMaster1;

    @Column(name = "report_type")
    private String reportType;

    @Transient
    @JsonProperty
    private String aifCalculation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getDetailsUpdatedFlag() {
        return detailsUpdatedFlag;
    }

    public void setDetailsUpdatedFlag(Integer detailsUpdatedFlag) {
        this.detailsUpdatedFlag = detailsUpdatedFlag;
    }

    public DistributorMaster getDistributorMaster1() {
        return distributorMaster1;
    }

    public void setDistributorMaster1(DistributorMaster distributorMaster1) {
        this.distributorMaster1 = distributorMaster1;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public List<RelationshipManager> getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(List<RelationshipManager> relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public List<DistributorMaster> getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(List<DistributorMaster> distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public RelationshipManager getRelationManage() {
        return relationManage;
    }

    public void setRelationManage(RelationshipManager relationManage) {
        this.relationManage = relationManage;
    }

    public String getAifCalculation() {
        return aifCalculation;
    }

    public void setAifCalculation(String aifCalculation) {
        this.aifCalculation = aifCalculation;
    }
}
