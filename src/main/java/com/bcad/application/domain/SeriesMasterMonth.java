package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "series_monthwise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SeriesMasterMonth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="series_id")
    private SeriesMaster seriesMaster;

    @Column(name="mnth_yr")
    private String monthYear;

    @Column(name="nav_value")
    private Float navValue;

    @Column(name="int_deleted")
    private Integer isDeleted;

    @ManyToOne
    @JoinColumn(name="file_upload_aif_id")
    private FileUploadAIF fileUploadAIF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Float getNavValue() {
        return navValue;
    }

    public void setNavValue(Float navValue) {
        this.navValue = navValue;
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
