package com.bcad.application.domain;

import java.util.Date;
import java.util.List;

public class ViewPaymentBean {

    private Date startDate;
    private Date endDate;
    private List<DistributorMaster> distributorMasterList;
    private List<RelationshipManager> relationshipManagerList;

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

    public List<DistributorMaster> getDistributorMasterList() {
        return distributorMasterList;
    }

    public void setDistributorMasterList(List<DistributorMaster> distributorMasterList) {
        this.distributorMasterList = distributorMasterList;
    }

    public List<RelationshipManager> getRelationshipManagerList() {
        return relationshipManagerList;
    }

    public void setRelationshipManagerList(List<RelationshipManager> relationshipManagerList) {
        this.relationshipManagerList = relationshipManagerList;
    }
}
