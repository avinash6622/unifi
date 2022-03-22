package com.bcad.application.domain;

import java.util.List;

public class ClientManagementSearch {

    List<String> clientCodes;
    List<String> clientNames;
    List<Long> distMasterId;
    List<Long> rmId;
    List<String> distModelType;
    List<Integer> distType;

    public List<String> getClientCodes() {
        return clientCodes;
    }

    public void setClientCodes(List<String> clientCodes) {
        this.clientCodes = clientCodes;
    }

    public List<String> getClientNames() {
        return clientNames;
    }

    public void setClientNames(List<String> clientNames) {
        this.clientNames = clientNames;
    }

    public List<Long> getDistMasterId() {
        return distMasterId;
    }

    public void setDistMasterId(List<Long> distMasterId) {
        this.distMasterId = distMasterId;
    }

    public List<Long> getRmId() {
        return rmId;
    }

    public void setRmId(List<Long> rmId) {
        this.rmId = rmId;
    }

    public List<String> getDistModelType() {
        return distModelType;
    }

    public void setDistModelType(List<String> distModelType) {
        this.distModelType = distModelType;
    }

    public List<Integer> getDistType() {
        return distType;
    }

    public void setDistType(List<Integer> distType) {
        this.distType = distType;
    }
}
