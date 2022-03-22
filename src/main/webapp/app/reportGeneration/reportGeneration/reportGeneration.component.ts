import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from './../../core/user/account.model';
import { ReportGeneration } from '../reportGeneration.model';
import { ReportGenerationService } from '../reportGeneration.service';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-reportgeneration',
    templateUrl: './reportGeneration.component.html'
})
export class ReportGenerationComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    reportGeneration: ReportGeneration;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    relations: any[];
    distributorMasters: any[];
    dropdownSettings: {};
    dropdownSettings1: {};
    thingsSubscription: Subscription;
    reportType: any;
    reportStatus: boolean;
    acctValue: any;

    constructor(
        private router: Router,
        private reportGenerationService: ReportGenerationService,
        private principal: Principal,
        private userService: UserService,
        private _location: Location
    ) {
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'distName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 10,
            allowSearchFilter: true
        };
        this.dropdownSettings1 = {
            singleSelection: false,
            idField: 'id',
            textField: 'rmName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 10,
            allowSearchFilter: true
        };
        this.principal.identity().then(account => {
            this.acctValue = account;
            console.log(this.acctValue, '123');
        });
    }

    ngOnInit() {
        this.reportGeneration = {};
        if (this.reportGeneration) {
            if (!this.reportGeneration.distributorMaster) {
                this.reportGeneration.distributorMaster = null;
            }

            if (!this.reportGeneration.relationshipManager) {
                this.reportGeneration.relationshipManager = null;
            }

            this.distributorMasters = [];
            this.userService.distributorsLogin().subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'distributorMasters');
            });

            this.relations = [];
            this.userService.relations().subscribe(relations => {
                this.relations = relations;
            });
        }
        console.log(this.acctValue, '123');
    }

    onDateChange(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.reportGeneration.startDate = event.target.value;
            console.log(this.reportGeneration.startDate, '123');
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.reportGeneration.toDate = event.target.value;
            console.log(this.reportGeneration.toDate, '123');
        }
    }

    onItemSelect(item: any) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        this.reportStatus = false;
        console.log(this.reportGeneration);
        this.thingsSubscription = this.reportGenerationService.save(this.reportGeneration).subscribe(
            data => {
                console.log('data', data);
                if (data) {
                    this.reportType = data;
                    this.router.navigate(['/']);
                    swal('Report Generated Successfully');
                } else {
                }
            },
            err => {
                console.log('Error::d', err);
                if (err.status) {
                    this.reportStatus = true;
                    swal('Report Generated Successfully');
                } else {
                    swal('Report Generation UnSuccessfully');
                }
            }
        );
    }
}
