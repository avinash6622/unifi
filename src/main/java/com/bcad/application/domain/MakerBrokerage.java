package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "maker_fee_brokerage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MakerBrokerage extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pms_client_id")
    private PMSClientMaster pmsClientMaster;

    @Column(name="brokerage_amt")
    private Float brokerageAmount;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="fileuploaded_id")
    private FileUpload fileUpload;

    @Column(name="int_deleted")
    private Integer isDeleted=0;

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

    public Float getBrokerageAmount() {
        return brokerageAmount;
    }

    public void setBrokerageAmount(Float brokerageAmount) {
        this.brokerageAmount = brokerageAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "MakerBrokerage{" +
            "id=" + id +
            ", pmsClientMaster=" + pmsClientMaster +
            ", brokerageAmount=" + brokerageAmount +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", fileUpload=" + fileUpload +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
