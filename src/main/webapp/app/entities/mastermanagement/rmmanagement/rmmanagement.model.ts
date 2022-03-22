export interface IRM {
    id?: any;
    rmName?: string;
    location?: any;
}

export class RelationshipManager implements IRM {
    constructor(public id?: any, public rmName?: string, public location?: any) {
        this.id = id ? id : null;
        this.rmName = rmName ? rmName : null;
        this.location = location ? location : null;
    }
}
