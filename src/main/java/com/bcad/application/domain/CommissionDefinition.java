package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bcad_commission_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CommissionDefinition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="commission_code")
    private String commissionCode;

    @Column(name="commission_name")
    private String commissionName;

    @Column(name="tenor_min_yr")
    private Float tenorMinYr;

    @Column(name="tenor_max_yr")
    private Float tenorMaxYr;

    @Column(name="start_year")
    private Date startYear;

    @Column(name="end_year")
    private Date endYear;

    @Column(name="upfront_per")
    private Float upfrontper;

    @Column(name="sec_upfront_per")
    private Float secUpfrontper;

    @Column(name="trial_per")
    private Float trialper;

    @Column(name="adjustment_per")
    private Float adjustmentper;

    @Column(name="sec_adjustment_yr")
    private Float secAdjustmentYr;

    @Column(name="adjustment_yr")
    private Float adjustmentyr;

    @Column(name="fee_type")
    private String feeType;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="bcad_pms")
    private Integer bcadPMS;

    @Column(name="pms_investment")
    private Integer pmsInvest;

    @Column(name="distributor_comm")
    private Float distributorComm;

    @Column(name="brokerage_comm")
    private Float brokerageComm;

    @Column(name="nav_comm")
    private Float navComm;

    @Column(name="profitshare_comm")
    private Float profitComm;

    @JsonProperty
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "bcad_comm_dist_map",
        joinColumns = @JoinColumn(name = "comm_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "dist_id", referencedColumnName = "id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DistributorMaster> distributorMasters = new HashSet<>();

    @JsonProperty
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "bcad_comm_dist_opt3",
        joinColumns = @JoinColumn(name = "comm_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "dist_id", referencedColumnName = "id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DistributorMaster> distributorMasterOption = new HashSet<>();


    @JsonProperty
    @OneToMany(mappedBy = "commissionDefinition",cascade={CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval=true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<CommissionDefinitionOptionMap> commissionDefinitionOptionMaps;


    @Column(name="trail_upfront_option")
    private String trailUpfrontOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommissionCode() {
        return commissionCode;
    }

    public void setCommissionCode(String commissionCode) {
       this.commissionCode = commissionCode;
    }

    public String getCommissionName() {
        return commissionName;
    }

    public void setCommissionName(String commissionName) {
        this.commissionName = commissionName;
    }

    public Float getTenorMinYr() {
        return tenorMinYr;
    }

    public void setTenorMinYr(Float tenorMinYr) {
        this.tenorMinYr = tenorMinYr;
    }

    public Float getTenorMaxYr() {
        return tenorMaxYr;
    }

    public void setTenorMaxYr(Float tenorMaxYr) {
        this.tenorMaxYr = tenorMaxYr;
    }

    public Float getUpfrontper() {
        return upfrontper;
    }

    public void setUpfrontper(Float upfrontper) {
        this.upfrontper = upfrontper;
    }

    public Float getTrialper() {
        return trialper;
    }

    public void setTrialper(Float trialper) {
        this.trialper = trialper;
    }

    public Float getAdjustmentper() {
        return adjustmentper;
    }

    public void setAdjustmentper(Float adjustmentper) {
        this.adjustmentper = adjustmentper;
    }

    public Float getAdjustmentyr() {
        return adjustmentyr;
    }

    public void setAdjustmentyr(Float adjustmentyr) {
        this.adjustmentyr = adjustmentyr;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<DistributorMaster> getDistributorMasters() {
        return distributorMasters;
    }

    public void setDistributorMasters(Set<DistributorMaster> distributorMasters) {
        this.distributorMasters = distributorMasters;
    }

    public CommissionDefinition distributorMasters(Set<DistributorMaster> distributorMasters1) {
        this.distributorMasters = distributorMasters1;
        return this;
    }

    public CommissionDefinition addDistributorMasters(DistributorMaster distributorMaster) {
        this.distributorMasters.add(distributorMaster);
        distributorMaster.getDistributorMasterMap().add(this);
        return this;
    }

    public CommissionDefinition removeDistributorMasters(DistributorMaster distributorMaster) {
        this.distributorMasters.remove(distributorMaster);
        distributorMaster.getDistributorMasterMap().add(this);
        return this;
    }

    public CommissionDefinition commissionDefinitionOptionMaps(List<CommissionDefinitionOptionMap> commissionDefinitionOptionMaps) {
        this.commissionDefinitionOptionMaps = commissionDefinitionOptionMaps;
        return this;
    }

    public CommissionDefinition addCommissionDefinitionOptionMap(CommissionDefinitionOptionMap commissionDefinitionOptionMap) {
        this.commissionDefinitionOptionMaps.add(commissionDefinitionOptionMap);
        commissionDefinitionOptionMap.setCommissionDefinition(this);
        return this;
    }

    public CommissionDefinition removeCommissionDefinitionOptionMap(CommissionDefinitionOptionMap commissionDefinitionOptionMap) {
        this.commissionDefinitionOptionMaps.remove(commissionDefinitionOptionMap);
        commissionDefinitionOptionMap.setCommissionDefinition(null);
        return this;
    }

    public List<CommissionDefinitionOptionMap> getCommissionDefinitionOptionMaps() {
        return commissionDefinitionOptionMaps;
    }

    public void setCommissionDefinitionOptionMaps(List<CommissionDefinitionOptionMap> commissionDefinitionOptionMaps) {
        this.commissionDefinitionOptionMaps = commissionDefinitionOptionMaps;
    }

    public Float getDistributorComm() {
        return distributorComm;
    }

    public void setDistributorComm(Float distributorComm) {
        this.distributorComm = distributorComm;
    }

    public Float getBrokerageComm() {
        return brokerageComm;
    }

    public void setBrokerageComm(Float brokerageComm) {
        this.brokerageComm = brokerageComm;
    }

    public Float getNavComm() {
        return navComm;
    }

    public void setNavComm(Float navComm) {
        this.navComm = navComm;
    }

    public Float getProfitComm() {
        return profitComm;
    }

    public void setProfitComm(Float profitComm) {
        this.profitComm = profitComm;
    }

    public Set<DistributorMaster> getDistributorMasterOption() {
        return distributorMasterOption;
    }

    public void setDistributorMasterOption(Set<DistributorMaster> distributorMasterOption) {
        this.distributorMasterOption = distributorMasterOption;
    }

    public CommissionDefinition distributorMasterOption(Set<DistributorMaster> distributorMasterOption1) {
        this.distributorMasterOption = distributorMasterOption1;
        return this;
    }

    public CommissionDefinition addDistributorMasterOption(DistributorMaster distributorMaster1) {
        this.distributorMasterOption.add(distributorMaster1);
        distributorMaster1.getDistributorMasterOption3().add(this);
        return this;
    }

    public CommissionDefinition removeDistributorMasterOption(DistributorMaster distributorMaster1) {
        this.distributorMasterOption.remove(distributorMaster1);
        distributorMaster1.getDistributorMasterOption3().add(this);
        return this;
    }

    public Date getStartYear() {
        return startYear;
    }

    public void setStartYear(Date startYear) {
        this.startYear = startYear;
    }

    public Date getEndYear() {
        return endYear;
    }

    public void setEndYear(Date endYear) {
        this.endYear = endYear;
    }

    public Float getSecUpfrontper() {
        return secUpfrontper;
    }

    public void setSecUpfrontper(Float secUpfrontper) {
        this.secUpfrontper = secUpfrontper;
    }

    public Float getSecAdjustmentYr() {
        return secAdjustmentYr;
    }

    public void setSecAdjustmentYr(Float secAdjustmentYr) {
        this.secAdjustmentYr = secAdjustmentYr;
    }

    public Integer getBcadPMS() {
        return bcadPMS;
    }

    public void setBcadPMS(Integer bcadPMS) {
        this.bcadPMS = bcadPMS;
    }

    public Integer getPmsInvest() {
        return pmsInvest;
    }

    public void setPmsInvest(Integer pmsInvest) {
        this.pmsInvest = pmsInvest;
    }

    public String getTrailUpfrontOption() {
        return trailUpfrontOption;
    }

    public void setTrailUpfrontOption(String trailUpfrontOption) {
        this.trailUpfrontOption = trailUpfrontOption;
    }

    @Override
    public String toString() {
        return "CommissionDefinition{" +
            "id=" + id +
            ", commissionCode='" + commissionCode + '\'' +
            ", commissionName='" + commissionName + '\'' +
            ", tenorMinYr=" + tenorMinYr +
            ", tenorMaxYr=" + tenorMaxYr +
            ", startYear=" + startYear +
            ", endYear=" + endYear +
            ", upfrontper=" + upfrontper +
            ", secUpfrontper=" + secUpfrontper +
            ", trialper=" + trialper +
            ", adjustmentper=" + adjustmentper +
            ", secAdjustmentYr=" + secAdjustmentYr +
            ", adjustmentyr=" + adjustmentyr +
            ", feeType='" + feeType + '\'' +
            ", location=" + location +
            ", product=" + product +
            ", bcadPMS=" + bcadPMS +
            ", pmsInvest=" + pmsInvest +
            ", distributorComm=" + distributorComm +
            ", brokerageComm=" + brokerageComm +
            ", navComm=" + navComm +
            ", profitComm=" + profitComm +
            ", distributorMasters=" + distributorMasters +
            ", distributorMasterOption=" + distributorMasterOption +
            ", commissionDefinitionOptionMaps=" + commissionDefinitionOptionMaps +
            '}';
    }
}
