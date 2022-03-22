import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-rmmanagement',
    templateUrl: './rmmanagement.component.html',
    styleUrls: ['./rmmanagement.component.css']
})
export class RmmanagementComponent implements OnInit {
    constructor() {}

    public isDisplayRM: Boolean = false;
    public isAddrm: false;
    public isUpdaterm: false;
    ngOnInit() {}
    addRM() {
        this.isDisplayRM = true;
    }
    addrm(e) {
        this.isUpdaterm = false;
        this.isAddrm = e.target.checked;
    }

    updaterm(e) {
        this.isAddrm = false;
        this.isUpdaterm = e.target.checked;
    }
}
