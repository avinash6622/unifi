import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-locationmanagement',
    templateUrl: './locationmanagement.component.html',
    styleUrls: ['./locationmanagement.component.css']
})
export class LocationmanagementComponent implements OnInit {
    constructor() {}
    public isDisplaylocation: boolean = false;
    public isAddLocation: false;
    public isUpdateLocation: false;
    ngOnInit() {}

    addclient() {
        this.isDisplaylocation = true;
    }
    addlocation(e) {
        this.isUpdateLocation = false;
        this.isAddLocation = e.target.checked;
    }

    updatelocation(e) {
        this.isAddLocation = false;
        this.isUpdateLocation = e.target.checked;
    }
}
