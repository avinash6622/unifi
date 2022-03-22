package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="bcad_client_commision")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientCommission extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private ClientManagement clientId;

    @Column(name="nav_comm")
    private Float navComm;

    @Column(name="profitshare_comm")
    private Float profitComm;

    @Column(name="update_required")
    private Integer updateRequired;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ClientManagement getClientId() {
        return clientId;
    }

    public void setClientId(ClientManagement clientId) {
        this.clientId = clientId;
    }
    
    public Integer getUpdateRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(Integer updateRequired) {
        this.updateRequired = updateRequired;
    }
}
