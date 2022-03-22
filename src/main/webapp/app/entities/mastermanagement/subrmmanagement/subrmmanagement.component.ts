import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-subrmmanagement',
    templateUrl: './subrmmanagement.component.html',
    styleUrls: ['./subrmmanagement.component.css']
})
export class SubrmmanagementComponent implements OnInit {
    constructor() {}

    public isDisplaysubrm: Boolean = false;
    public isAddsubrm: false;
    public isUpdatesubrm: false;
    ngOnInit() {}
    addClient() {
        this.isDisplaysubrm = true;
    }
    addsubrm(e) {
        this.isUpdatesubrm = false;
        this.isAddsubrm = e.target.checked;
    }

    updatesubrm(e) {
        this.isAddsubrm = false;
        this.isUpdatesubrm = e.target.checked;
    }
}
