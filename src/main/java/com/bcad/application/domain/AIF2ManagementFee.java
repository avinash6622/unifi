package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aif2_management_fee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIF2ManagementFee extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mnth_year")
    private Date monthYear;


    @Column(name = "units")
    private Float units;

    public Date getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(Date monthYear) {
        this.monthYear = monthYear;
    }

    @ManyToOne
    @JoinColumn(name="series_master")
    private AIF2SeriesMaster aif2SeriesMaster;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public AIF2SeriesMaster getAif2SeriesMaster() {
        return aif2SeriesMaster;
    }

    public void setAif2SeriesMaster(AIF2SeriesMaster aif2SeriesMaster) {
        this.aif2SeriesMaster = aif2SeriesMaster;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
