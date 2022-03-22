export interface ClientCommission {
    id?: number;
    pmsCommission?: number;
    navCommission?: number;
    brokerageCommission?: number;
    profitCommission?: number;
    aifCommission?: number;
    corpusCommission?: number;
    pmsClientMaster?: any;

    /* clientCode ?: any;
    clientName ?: any;
    panNo?:any;
    distributorMaster:{
        id:''
    };
    product : {
        id : '';
        productNam : ''
      };

       relationshipManager : {
          id :'' ,
          rmName : '',
          location : '',
          subRMS : [ {
            id : '',
            subName : '',
            subRMMap : ''
          } ]
        }*/
}
