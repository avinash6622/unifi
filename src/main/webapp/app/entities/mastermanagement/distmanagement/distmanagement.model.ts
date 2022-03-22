export interface IDistributor {
    id?: any;
    distName?: string;
    distributorType?: any;
    relationshipManager?: any;
    location?: any;
}

export class DistributorMaster implements IDistributor {
    constructor(
        public id?: any,
        public distName?: string,
        public distributorType?: any,
        public relationshipManager?: any,
        public location?: any
    ) {
        this.id = id ? id : null;
        this.distName = distName ? distName : null;
        this.distributorType = distributorType ? distributorType : null;
        this.relationshipManager = relationshipManager ? relationshipManager : null;
        this.location = location ? location : null;
    }
}
