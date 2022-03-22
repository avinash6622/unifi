import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-distmanagement',
    templateUrl: './distmanagement.component.html',
    styleUrls: ['./distmanagement.component.css']
})
export class DistmanagementComponent implements OnInit {
    constructor() {}

    public isDisplaydisbutor: Boolean = false;
    public isAdddisb: false;
    public isUpdatedisb: false;
    ngOnInit() {}
    addClient() {
        this.isDisplaydisbutor = true;
    }
    adddisb(e) {
        this.isUpdatedisb = false;
        this.isAdddisb = e.target.checked;
    }

    updatedisb(e) {
        this.isAdddisb = false;
        this.isUpdatedisb = e.target.checked;
    }
}
