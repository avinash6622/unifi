import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from './../../core/user/account.model';
import { Reportdeletion } from '../reportdeletion.model';
import { ReportDeletionService } from '../reportdeletion.service';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-reportdeletion',
    templateUrl: './reportdeletion.component.html'
})
export class ReportDeletionComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    report: Reportdeletion;
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
    record: any;

    constructor(
        private router: Router,
        private principal: Principal,
        private userService: UserService,
        private _location: Location,
        private reportdeletionService: ReportDeletionService
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
        this.report = {};
        if (this.report) {
            if (!this.report.distributorMasterList) {
                this.report.distributorMasterList = null;
            }
            this.distributorMasters = [];
            this.userService.distributors().subscribe(distributorMasters => {
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
            this.report.startDate = event.target.value;
            console.log(this.report.startDate, '123');
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.report.endDate = event.target.value;
            console.log(this.report.endDate, '123');
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
        console.log(this.report.distributorMasterList.length);
        console.log(this.report.relationshipManagerList, '123');
        this.thingsSubscription = this.reportdeletionService.save(this.report).subscribe(
            data => {
                console.log('data', data);
                this.record = data;
                if (this.record === null) {
                    this.reportStatus = true;
                    this.router.navigate(['/reportdeletion']);
                    swal('Report Deleted Successfully');
                } else {
                }
            },
            err => {
                console.log('Error::d', err);
                if (err.status) {
                    this.reportStatus = true;
                    swal('Report Deleted  Successfully');
                } else {
                    swal('Report Deleted  UnSuccessfully');
                }
            }
        );
    }
}
