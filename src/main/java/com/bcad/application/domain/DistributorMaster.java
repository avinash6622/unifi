package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dist_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DistributorMaster extends AbstractAuditingEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="dist_name")
    private String distName;

    @Column(name="pms_comm")
    private Float pmsCommission;

    @Column(name="aif_comm")
    private Float aifCommission;

    @Column(name="aif2_comm")
    private Float aif2Commission;

    @Column(name="dist_model_type")
    private String distModelType;

    @Column(name="aifblend_comm")
    private Float aifBlendComm;

    public Float getAifBlendComm() {
        return aifBlendComm;
    }

    public void setAifBlendComm(Float aifBlendComm) {
        this.aifBlendComm = aifBlendComm;
    }

    @ManyToOne
    @JoinColumn(name = "dist_type_id")
    private DistributorType distributorType;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    @ManyToMany(mappedBy = "distributorMasters",fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CommissionDefinition> distributorMasterMap = new HashSet<>();

    @ManyToMany(mappedBy = "distributorMasterOption",fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CommissionDefinition> distributorMasterOption3 = new HashSet<>();


    // @Transient
    // RelationshipManager relationshipManager1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public DistributorType getDistributorType() {
        return distributorType;
    }

    public void setDistributorType(DistributorType distributorType) {
        this.distributorType = distributorType;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Float getPmsCommission() {
        return pmsCommission;
    }

    public void setPmsCommission(Float pmsCommission) {
        this.pmsCommission = pmsCommission;
    }

    public Float getAifCommission() {
        return aifCommission;
    }

    public void setAifCommission(Float aifCommission) {
        this.aifCommission = aifCommission;
    }

    public Float getAif2Commission() {
        return aif2Commission;
    }

    public void setAif2Commission(Float aif2Commission) {
        this.aif2Commission = aif2Commission;
    }

    public String getDistModelType() {
        return distModelType;
    }

    public void setDistModelType(String distModelType) {
        this.distModelType = distModelType;
    }

    public Set<CommissionDefinition> getDistributorMasterMap() {
        return distributorMasterMap;
    }

    public void setDistributorMasterMap(Set<CommissionDefinition> distributorMasterMap) {
        this.distributorMasterMap = distributorMasterMap;
    }

    public DistributorMaster distributorMasterMap(Set<CommissionDefinition> commissionDefinitions) {
        this.distributorMasterMap = commissionDefinitions;
        return this;
    }

    public DistributorMaster addDistributorMasterMap(CommissionDefinition commissionDefinition) {
        this.distributorMasterMap.add(commissionDefinition);
        commissionDefinition.getDistributorMasters().add(this);
        return this;
    }

    public DistributorMaster removeDistributorMasterMap(CommissionDefinition commissionDefinition) {
        this.distributorMasterMap.remove(commissionDefinition);
        commissionDefinition.getDistributorMasters().remove(this);
        return this;
    }

    public Set<CommissionDefinition> getDistributorMasterOption3() {
        return distributorMasterOption3;
    }

    public void setDistributorMasterOption3(Set<CommissionDefinition> distributorMasterOption3) {
        this.distributorMasterOption3 = distributorMasterOption3;
    }

    public DistributorMaster distributorMasterOption3(Set<CommissionDefinition> commissionDefinition) {
        this.distributorMasterOption3 = commissionDefinition;
        return this;
    }

    public DistributorMaster addDistributorMasterOption3(CommissionDefinition commissionDefinition) {
        this.distributorMasterOption3.add(commissionDefinition);
        commissionDefinition.getDistributorMasterOption().add(this);
        return this;
    }

    public DistributorMaster removeDistributorMasterOption3(CommissionDefinition commissionDefinition) {
        this.distributorMasterOption3.remove(commissionDefinition);
        commissionDefinition.getDistributorMasterOption().remove(this);
        return this;
    }

   /* @Override
    public String toString() {
        return "DistributorMaster{" +
            "id=" + id +
            ", distName='" + distName + '\'' +
            ", pmsCommission=" + pmsCommission +
            ", aifCommission=" + aifCommission +
            ", distModelType='" + distModelType + '\'' +
            ", distributorType=" + distributorType +
            ", relationshipManager=" + relationshipManager +
            ", location=" + location +
            '}';
    }*/
}
