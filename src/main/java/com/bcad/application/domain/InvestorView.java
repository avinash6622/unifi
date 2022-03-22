package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "investor_view")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvestorView implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mnth_yr")
    private String monthYear;

    @ManyToOne
    @JoinColumn(name="series_id")
    private SeriesMaster seriesMaster;

    @Column(name="no_of_units")
    private Float noOfUnits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public Float getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Float noOfUnits) {
        this.noOfUnits = noOfUnits;
    }
}
