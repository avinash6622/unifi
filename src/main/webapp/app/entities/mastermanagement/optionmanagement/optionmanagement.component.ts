import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-optionmanagement',
    templateUrl: './optionmanagement.component.html',
    styleUrls: ['./optionmanagement.component.css']
})
export class OptionmanagementComponent implements OnInit {
    constructor() {}

    public isDisplayoption: Boolean = false;
    public isAddoption: false;
    public isUpdateoption: false;
    ngOnInit() {}
    addClient() {
        this.isDisplayoption = true;
    }
    addoption(e) {
        this.isUpdateoption = false;
        this.isAddoption = e.target.checked;
    }

    updateoption(e) {
        this.isAddoption = false;
        this.isUpdateoption = e.target.checked;
    }
}
