package com.bcad.application.service.dto;

import com.bcad.application.domain.*;
import java.util.Date;

public class ClientInvestmentDateDTO {
    private String clientCode;
    private Date investmentDate;
    private String schemeName;

    public ClientInvestmentDateDTO() {
        // Empty constructor needed for Jackson.
    }

  /*  public ClientInvestmentDateDTO(ProfitShare profitShare) {
        this.clientCode = pmsNav.getCodeScheme();
        this.investmentDate = pmsNav.getInvestmentDate();
        this.schemeName = pms
    }*/

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Date getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
}
