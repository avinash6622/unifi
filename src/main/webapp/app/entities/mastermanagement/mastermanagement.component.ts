import { Component, OnInit } from '@angular/core';
import { temporaryAllocator } from '@angular/compiler/src/render3/view/util';

@Component({
    selector: 'jhi-app-mastermanagement',
    templateUrl: './mastermanagement.component.html',
    styleUrls: ['./mastermanagement.component.css']
})
export class MastermanagementComponent implements OnInit {
    bgcolors: boolean = true;
    containerActive: boolean = false;

    constructor() {}

    ngOnInit() {}

    // setBgColor() {

    //     this.bgcolors = !this.bgcolors;
    // }

    //     clicked(event) {
    //         console.log('1');
    //         this.containerActive=!this.containerActive
    //    }
}
