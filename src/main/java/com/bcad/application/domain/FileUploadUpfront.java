package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file_upload_upfront")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileUploadUpfront extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "store_file_location")
    private String stroreFileLocation;

    @Column(name = "upload_approved")
    private Integer uploadApproved;

    @ManyToOne
    @JoinColumn(name = "file_type_id")
    private FileType fileType;

    @Column(name = "int_deleted")
    private Integer isDeleted;

    @Transient
    private String status;

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

    @Transient
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStroreFileLocation() {
        return stroreFileLocation;
    }

    public void setStroreFileLocation(String stroreFileLocation) {
        this.stroreFileLocation = stroreFileLocation;
    }

    public Integer getUploadApproved() {
        return uploadApproved;
    }

    public void setUploadApproved(Integer uploadApproved) {
        this.uploadApproved = uploadApproved;
    }

    public FileType FileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
