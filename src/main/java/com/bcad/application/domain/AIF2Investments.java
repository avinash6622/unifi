package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aif2_calculations")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIF2Investments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="aif2_client_id")
    private ClientManagement clientManagement;

    @ManyToOne
    @JoinColumn(name="aif2_series_id")
    private AIF2SeriesMaster aif2SeriesMaster;

    @Column(name="tot_of_units")
    private Float totOfUnits;

    @Column(name="unit_price")
    private Float unitPrice;

    @Column(name="no_of_units")
    private Float noOfUnits;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientManagement getClientManagement() {
        return clientManagement;
    }

    public void setClientManagement(ClientManagement clientManagement) {
        this.clientManagement = clientManagement;
    }

    public Float getTotOfUnits() {
        return totOfUnits;
    }

    public void setTotOfUnits(Float totOfUnits) {
        this.totOfUnits = totOfUnits;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(Float noOfUnits) {
        this.noOfUnits = noOfUnits;
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
