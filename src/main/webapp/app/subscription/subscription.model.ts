export interface Subscription {
    id?: any;
    paymentType?: string;
    subscriptionDate?: any;
    seriesMaster?: any;
    initPerCost?: any;
    navAsOn?: any;
    clients?: ClientCode[];
}

export interface ClientCode {
    aifClientMaster?: any;
    aifClientMaster1?: any;
    aifDist?: any;
    noOfUnits?: any;
    protfolioValue?: any;
    seriesMaster?: any;
    monthYr?: any;
    costInvestment?: any;
    subscriptionDate?: any;
    navAsOn?: any;
    distributorMaster?: any;
}
