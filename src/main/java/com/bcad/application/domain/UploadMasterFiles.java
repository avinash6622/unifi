package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="bcad_upload_master_files")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UploadMasterFiles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "master_type_id")
    private MasterType masterType;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "int_deleted")
    private Integer isDeleted;

    @Column(name = "fee_from_date")
    private Date feeFromDate;

    @Column(name = "fee_to_date")
    private Date feeToDate;

    @Column(name="file_location")
    private String fileLocation;

    @Column(name="int_approved")
    private Integer uploadApproved;

    @Transient
    private String status;
    @Transient
    private String code;
    @Transient
    private String description;

    public Long getId() {
        return id;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MasterType getMasterType() {
        return masterType;
    }

    public void setMasterType(MasterType masterType) {
        this.masterType = masterType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getFeeFromDate() {
        return feeFromDate;
    }

    public void setFeeFromDate(Date feeFromDate) {
        this.feeFromDate = feeFromDate;
    }

    public Date getFeeToDate() {
        return feeToDate;
    }

    public void setFeeToDate(Date feeToDate) {
        this.feeToDate = feeToDate;
    }

    public Integer getUploadApproved() {
        return uploadApproved;
    }

    public void setUploadApproved(Integer uploadApproved) {
        this.uploadApproved = uploadApproved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override


    public String toString() {
        return "UploadMasterFiles{" +
            "id=" + id +
            ", masterType=" + masterType +
            ", location=" + location +
            ", product=" + product +
            ", isDeleted=" + isDeleted +
            ", feeFromDate=" + feeFromDate +
            ", feeToDate=" + feeToDate +
            ", fileLocation='" + fileLocation + '\'' +
            ", uploadApproved=" + uploadApproved +
            ", status='" + status + '\'' +
            ", code='" + code + '\'' +
            ", description='" + description + '\'' +
            '}';

    }
}

