package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aif_updated_units")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIFUpdatedUnits extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="aif_client_id")
    private AIFClientMaster aifClientMaster;

    @ManyToOne
    @JoinColumn(name="series_id")
    private SeriesMaster seriesMaster;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @Column(name="tot_rem_units")
    private Float totRemUnits;

    @Column(name="mnthyr")
    private String monthYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AIFClientMaster getAifClientMaster() {
        return aifClientMaster;
    }

    public void setAifClientMaster(AIFClientMaster aifClientMaster) {
        this.aifClientMaster = aifClientMaster;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public Float getTotRemUnits() {
        return totRemUnits;
    }

    public void setTotRemUnits(Float totRemUnits) {
        this.totRemUnits = totRemUnits;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
