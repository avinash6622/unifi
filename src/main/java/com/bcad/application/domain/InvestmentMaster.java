package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="investment_master")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvestmentMaster extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="investment_code")
    private String investmentCode;

    @Column(name="investment_name")
    private String investmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvestmentCode() {
        return investmentCode;
    }

    public void setInvestmentCode(String investmentCode) {
        this.investmentCode = investmentCode;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }
}
