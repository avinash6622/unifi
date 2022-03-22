import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-strategymgmt',
    templateUrl: './strategymgmt.component.html',
    styleUrls: ['./strategymgmt.component.css']
})
export class StrategymgmtComponent implements OnInit {
    constructor() {}

    public isDisplaystrategy: Boolean = false;
    public isAddstrategy: false;
    public isUpdatestrategy: false;
    ngOnInit() {}
    addClient() {
        this.isDisplaystrategy = true;
    }
    addstrategy(e) {
        this.isUpdatestrategy = false;
        this.isAddstrategy = e.target.checked;
    }

    updatestrategy(e) {
        this.isAddstrategy = false;
        this.isUpdatestrategy = e.target.checked;
    }
}
