package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="client_fee_commision")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientFeeCommission extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pms_client_id")
    private PMSClientMaster pmsClientMaster;

    @Column(name="nav_comm")
    private Float navComm;

    @Column(name="brokerage_comm")
    private Float brokerageComm;

    @Column(name="profitshare_comm")
    private Float profitComm;

    @Column(name="aif_comm")
    private Float aifComm;

    @Column(name="corpus_comm")
    private Float corpusComm;

    @Column(name="update_required")
    private Integer updateRequired;

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

    public Float getNavComm() {
        return navComm;
    }

    public void setNavComm(Float navComm) {
        this.navComm = navComm;
    }

    public Float getBrokerageComm() {
        return brokerageComm;
    }

    public void setBrokerageComm(Float brokerageComm) {
        this.brokerageComm = brokerageComm;
    }

    public Float getProfitComm() {
        return profitComm;
    }

    public void setProfitComm(Float profitComm) {
        this.profitComm = profitComm;
    }

    public Float getAifComm() {
        return aifComm;
    }

    public void setAifComm(Float aifComm) {
        this.aifComm = aifComm;
    }

    public Float getCorpusComm() {
        return corpusComm;
    }

    public void setCorpusComm(Float corpusComm) {
        this.corpusComm = corpusComm;
    }
    
    public Integer getUpdateRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(Integer updateRequired) {
        this.updateRequired = updateRequired;
    }
}
