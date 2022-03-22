// import { Component, OnInit } from '@angular/core';
// import { Commissiondefinitionmgmt } from './commissiondefinitionmgmt.service';

// @Component({
//     selector: 'jhi-app-commissiondefinitionmgmt',
//     templateUrl: './commissiondefinitionmgmt.component.html',
//     styleUrls: ['./commissiondefinitionmgmt.component.css']
// })
// export class CommissiondefinitionmgmtComponent {
//     public isDisplaystrategy: Boolean = false;
//     public isAddstrategy: false;
//     public isUpdatestrategy: false;
//     ngOnInit() {}

//     addClient() {
//         this.isDisplaystrategy = true;
//     }

//     addstrategy(e) {
//         this.isUpdatestrategy = false;
//         this.isAddstrategy = e.target.checked;
//     }

//     updatestrategy(e) {
//         this.isAddstrategy = false;
//         this.isUpdatestrategy = e.target.checked;
//     }
// }
import { Component, OnInit } from '@angular/core';
import { Commissiondefinitionmgmt } from './commissiondefinitionmgmt.service';

@Component({
    selector: 'jhi-app-commissiondefinitionmgmt',
    templateUrl: './commissiondefinitionmgmt.component.html',
    styleUrls: ['./commissiondefinitionmgmt.component.css']
})
export class CommissiondefinitionmgmtComponent {
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
