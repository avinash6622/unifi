package com.bcad.application.service.dto;

import com.bcad.application.domain.*;

public class ClientManagementDTO {
    private String clientCode;
    private String fullClientCode;
    private String clientName;

    public ClientManagementDTO() {
        // Empty constructor needed for Jackson.
    }

    public ClientManagementDTO(ClientManagement clientManagement) {
        this.clientCode = clientManagement.getClientCode();
        this.clientName = clientManagement.getClientName();
        this.fullClientCode = clientManagement.getClientCode();
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getFullClientCode() {
        return fullClientCode;
    }

    public void setFullClientCode(String fullClientCode) {
        this.fullClientCode = fullClientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
