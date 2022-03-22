import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from './../../core/user/account.model';
import { ViewPayment } from '../viewpayment.model';
import { ViewPaymentService } from '../viewpayment.service';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-viewpayment',
    templateUrl: './viewpayment.component.html'
})
export class ViewPaymentComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    viewpayment: ViewPayment;
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
        private viewPaymentService: ViewPaymentService,
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
        this.viewpayment = {};
        if (this.viewpayment) {
            if (!this.viewpayment.distributorMasterList) {
                this.viewpayment.distributorMasterList = null;
            }
            this.distributorMasters = [];
            this.userService.distributorsViewPayment().subscribe(distributorMasters => {
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
        if (event.target.value.match(/\d{4}-\d{2}-\d{2}/)) {
            this.viewpayment.startDate = event.target.value;
            console.log(this.viewpayment.startDate, '123');
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{4}-\d{2}-\d{2}/)) {
            this.viewpayment.endDate = event.target.value;
            console.log(this.viewpayment.endDate, '123');
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
        console.log(this.viewpayment.distributorMasterList.length);
        console.log(this.viewpayment.relationshipManagerList, '123');
        this.thingsSubscription = this.viewPaymentService.save(this.viewpayment).subscribe(
            data => {
                console.log('data', data);
                this.record = data;
                if (this.record === null) {
                    this.reportStatus = true;
                    this.router.navigate(['/viewpayment']);
                    swal('Payment Report Generated Successfully');
                } else {
                }
            },
            err => {
                console.log('Error::d', err);
                if (err.status) {
                    this.reportStatus = true;
                    swal('Payment Report Successfully');
                } else {
                    swal('Payment Report UnSuccessfully');
                }
            }
        );
    }
}
