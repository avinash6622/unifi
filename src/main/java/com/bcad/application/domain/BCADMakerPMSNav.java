package com.bcad.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bcad_maker_fee_pms_nav")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BCADMakerPMSNav extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="bcad_client_id")
    private ClientManagement clientManagement;

    @ManyToOne
    @JoinColumn(name="investment_id")
    private InvestmentMaster investmentMaster;

    @Column(name="investment_date")
    private Date investmentDate;

    @Column(name="client_code_scheme")
    private String codeScheme;

    @ManyToOne
    @JoinColumn(name="dist_id")
    private DistributorMaster distributorMaster;

    @ManyToOne
    @JoinColumn(name="rm_id")
    private RelationshipManager relationshipManager;

    @Column(name="selected_start_date")
    private Date selectedStartDate;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="no_days")
    private Integer noDays;

    @Column(name="percentage_comm")
    private Float percentageComm;

    @Column(name="gross_pms_nav")
    private Float grossPmsNav;

    @Column(name="margin_value")
    private Float marginValue;

    @Column(name="net_pms_nav")
    private Float netPmsNav;

    @Column(name="calc_pms_nav")
    private Float calcPmsNav;

    @Column(name="investment_mode")
    private String investMode;

    @Column(name="no_month")
    private Integer noMonth;

    @Column(name="no_year")
    private Integer noYear;

    @ManyToOne
    @JoinColumn(name="fileuploaded_id")
    private UploadMasterFiles fileUpload;

    @Column(name="int_deleted")
    private Integer isDeleted;

   /* @Column(name="client_comm")
    private float clientcomm;
*/
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

    public String getCodeScheme() {
        return codeScheme;
    }

    public void setCodeScheme(String codeScheme) {
        this.codeScheme = codeScheme;
    }

    public DistributorMaster getDistributorMaster() {
        return distributorMaster;
    }

    public void setDistributorMaster(DistributorMaster distributorMaster) {
        this.distributorMaster = distributorMaster;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public Date getSelectedStartDate() {
        return selectedStartDate;
    }

    public void setSelectedStartDate(Date selectedStartDate) {
        this.selectedStartDate = selectedStartDate;
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

    public Integer getNoDays() {
        return noDays;
    }

    public void setNoDays(Integer noDays) {
        this.noDays = noDays;
    }

    public Float getPercentageComm() {
        return percentageComm;
    }

    public void setPercentageComm(Float percentageComm) {
        this.percentageComm = percentageComm;
    }

    public Float getGrossPmsNav() {
        return grossPmsNav;
    }

    public void setGrossPmsNav(Float grossPmsNav) {
        this.grossPmsNav = grossPmsNav;
    }

    public Float getMarginValue() {
        return marginValue;
    }

    public void setMarginValue(Float marginValue) {
        this.marginValue = marginValue;
    }

    public Float getNetPmsNav() {
        return netPmsNav;
    }

    public void setNetPmsNav(Float netPmsNav) {
        this.netPmsNav = netPmsNav;
    }

    public Float getCalcPmsNav() {
        return calcPmsNav;
    }

    public void setCalcPmsNav(Float calcPmsNav) {
        this.calcPmsNav = calcPmsNav;
    }

    public String getInvestMode() {
        return investMode;
    }

    public void setInvestMode(String investMode) {
        this.investMode = investMode;
    }

    public Integer getNoMonth() {
        return noMonth;
    }

    public void setNoMonth(Integer noMonth) {
        this.noMonth = noMonth;
    }

    public Integer getNoYear() {
        return noYear;
    }

    public void setNoYear(Integer noYear) {
        this.noYear = noYear;
    }

    public UploadMasterFiles getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadMasterFiles fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public InvestmentMaster getInvestmentMaster() {
        return investmentMaster;
    }

    public void setInvestmentMaster(InvestmentMaster investmentMaster) {
        this.investmentMaster = investmentMaster;
    }

    public Date getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

    /*public float getClientcomm() {
        return clientcomm;
    }

    public void setClientcomm(float clientcomm) {
        this.clientcomm = clientcomm;
    }*/
}
