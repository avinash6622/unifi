package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fee_mange_perf_month")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AIFManagePerMonth implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "management_fee")
    private Float managementFee;

    @Column(name = "performance_fee")
    private Float performanceFee;

    @OneToOne
    @JoinColumn(name = "series_id")
    private SeriesMaster seriesMaster;

    @Column(name = "as_on_date")
    private Date asOnDate;

    @Column(name = "int_deleted")
    private Integer isDeleted;

    @ManyToOne
    @JoinColumn(name = "file_upload_aif_id")
    private FileUploadAIF fileUploadAIF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(Float managementFee) {
        this.managementFee = managementFee;
    }

    public Float getPerformanceFee() {
        return performanceFee;
    }

    public void setPerformanceFee(Float performanceFee) {
        this.performanceFee = performanceFee;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public FileUploadAIF getFileUploadAIF() {
        return fileUploadAIF;
    }

    public void setFileUploadAIF(FileUploadAIF fileUploadAIF) {
        this.fileUploadAIF = fileUploadAIF;
    }
}
