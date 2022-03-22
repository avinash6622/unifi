import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-app-feemgmt',
    templateUrl: './feemgmt.component.html',
    styleUrls: ['./feemgmt.component.css']
})
export class FeemgmtComponent implements OnInit {
    feetype: String;
    strategy: String;

    constructor() {}

    ngOnInit() {}

    reset() {
        console.log('reset works...');
        this.feetype = null;
        this.strategy = null;
    }
}
