package com.bcad.application.web.rest.errors;

public class InvestmentNotFound extends BadRequestAlertException {

    public InvestmentNotFound() {
        super(ErrorConstants.INVESTMENT_NOT_FOUND, "Investment Not found", "fileUpload", "investmentexists");
    }
}
