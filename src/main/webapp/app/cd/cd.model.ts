export class Option {
    public id?: any;
    public distributorOption?: object;
    public feeCalculation?: string;
}

export interface Cd {
    id?: number;
    commissionName?: string;
    commissionCode?: string;
    tenorMinYr?: number;
    tenorMaxYr?: number;
    startYear?: Date;
    endYear?: Date;
    upfrontper?: number;
    trialper?: number;
    adjustmentper?: number;
    adjustmentyr?: number;
    secUpfrontper?: number;
    secAdjustmentYr?: number;
    feeCalculation?: string;
    distributorMaster?: any;
    product?: any;
    relationshipManager?: any;
    location?: any;
    check1?: any;
    check2?: any;
    check3?: any;
    check4?: any;
    distributorComm?: any;
    distributorMasters?: any;
    relationshipManagers?: any;
    distributorOption?: any;
    brokerageComm?: any;
    navComm?: any;
    profitComm?: any;
    bcadPMS?: any;
    pmsInvest?: any;
    distributorMasterOption?: any;
    commissionDefinitionOptionMaps?: Option[];
}
