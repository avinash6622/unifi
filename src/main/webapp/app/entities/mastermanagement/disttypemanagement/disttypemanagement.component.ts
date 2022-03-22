import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-disttypemanagement',
    templateUrl: './disttypemanagement.component.html',
    styleUrls: ['./disttypemanagement.component.css']
})
export class DisttypemanagementComponent implements OnInit {
    constructor() {}

    public isDisplaydistype: Boolean = false;
    public isAdddisbtype: false;
    public isUpdatedisbtype: false;
    ngOnInit() {}
    addClient() {
        this.isDisplaydistype = true;
    }
    adddisbtype(e) {
        this.isUpdatedisbtype = false;
        this.isAdddisbtype = e.target.checked;
    }

    updatedisbtype(e) {
        this.isAdddisbtype = false;
        this.isUpdatedisbtype = e.target.checked;
    }
}
