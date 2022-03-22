package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bcad_comm_option_map")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CommissionDefinitionOptionMap implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="option_id")
    private DistributorOption distributorOption;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comm_id")
    @JsonBackReference
    private CommissionDefinition commissionDefinition;

    @Column(name="fee_calculation")
    private String feeCalculation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributorOption getDistributorOption() {
        return distributorOption;
    }

    public void setDistributorOption(DistributorOption distributorOption) {
        this.distributorOption = distributorOption;
    }

    public CommissionDefinition getCommissionDefinition() {
        return commissionDefinition;
    }

    public void setCommissionDefinition(CommissionDefinition commissionDefinition) {
        this.commissionDefinition = commissionDefinition;
    }

    public CommissionDefinitionOptionMap commissionDefinition(CommissionDefinition commissionDefinitions) {
        this.commissionDefinition = commissionDefinitions;
        return this;
    }

    public String getFeeCalculation() {
        return feeCalculation;
    }

    public void setFeeCalculation(String feeCalculation) {
        this.feeCalculation = feeCalculation;
    }
}
