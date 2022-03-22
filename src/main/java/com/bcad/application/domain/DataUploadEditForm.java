package com.bcad.application.domain;

import java.io.File;
import java.util.Date;
import java.util.List;

public class DataUploadEditForm {

    private byte[] fileStream;

    private String fileName;
    private File file;
    private String sFee;
    /* private File file; */
    private List<MasterLogBean> masterLogBean;
    private Date startDate;
    private Date endDate;
    private boolean enableSavePMSNav;
    private Boolean enablePMSDownload;
    //  private StreamedContent dFile;
    //private List<String> fileDownload=new ArrayList<String>();


    public byte[] getFileStream() {
        return fileStream;
    }

    public void setFileStream(byte[] fileStream) {
        this.fileStream = fileStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getsFee() {
        return sFee;
    }

    public void setsFee(String sFee) {
        this.sFee = sFee;
    }

    public List<MasterLogBean> getMasterLogBean() {
        return masterLogBean;
    }

    public void setMasterLogBean(List<MasterLogBean> masterLogBean) {
        this.masterLogBean = masterLogBean;
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

    public boolean isEnableSavePMSNav() {
        return enableSavePMSNav;
    }

    public void setEnableSavePMSNav(boolean enableSavePMSNav) {
        this.enableSavePMSNav = enableSavePMSNav;
    }

    public Boolean getEnablePMSDownload() {
        return enablePMSDownload;
    }

    public void setEnablePMSDownload(Boolean enablePMSDownload) {
        this.enablePMSDownload = enablePMSDownload;
    }
}
