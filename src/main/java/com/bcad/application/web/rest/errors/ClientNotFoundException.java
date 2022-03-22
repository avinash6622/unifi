package com.bcad.application.web.rest.errors;

public class ClientNotFoundException extends BadRequestAlertException {

    public ClientNotFoundException(Integer clientCode) {
        super(ErrorConstants.CLIENT_NOT_FOUND, "Client Not found"+clientCode, "fileUpload", "clientexists");
    }

}
