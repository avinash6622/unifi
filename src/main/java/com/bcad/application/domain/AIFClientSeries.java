package com.bcad.application.domain;

public class AIFClientSeries {

    private AIFClientMaster aifClientMaster;
    private SeriesMaster seriesMaster;

    public AIFClientMaster getAifClientMaster() {
        return aifClientMaster;
    }

    public void setAifClientMaster(AIFClientMaster aifClientMaster) {
        this.aifClientMaster = aifClientMaster;
    }

    public SeriesMaster getSeriesMaster() {
        return seriesMaster;
    }

    public void setSeriesMaster(SeriesMaster seriesMaster) {
        this.seriesMaster = seriesMaster;
    }
}
