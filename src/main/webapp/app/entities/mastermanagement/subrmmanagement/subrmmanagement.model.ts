export interface ISubRM {
    id?: any;
    subRMName?: string;
    relationshipManager?: any;
}

export class DistributorMaster implements ISubRM {
    constructor(public id?: any, public subRMName?: string, public relationshipManager?: any) {
        this.id = id ? id : null;
        this.subRMName = subRMName ? subRMName : null;
        this.relationshipManager = relationshipManager ? relationshipManager : null;
    }
}
