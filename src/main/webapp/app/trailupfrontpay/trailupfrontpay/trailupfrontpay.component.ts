import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { TrailUpfrontPayService } from '../trailupfrontpay.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from '../../core/user/account.model';
import { TrailUpfrontPay } from '../trailupfrontpay.model';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-trailupfrontpay',
    templateUrl: './trailupfrontpay.component.html'
})
export class TrailUpfrontPayComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    trailupfrontpay: TrailUpfrontPay;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    distributorMasters: any[];
    detail: any;
    dropdownSettings: any;
    distributorMaster: any;

    constructor(
        private router: Router,
        private trailupfrontpayService: TrailUpfrontPayService,
        private principal: Principal,
        private userService: UserService,
        private _location: Location
    ) {
        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'distName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 10,
            allowSearchFilter: true
        };
    }

    ngOnInit() {
        this.trailupfrontpay = {};
        if (this.trailupfrontpay) {
            if (!this.trailupfrontpay.distributorMaster) {
                this.trailupfrontpay.distributorMaster = null;
                console.log('null');
            }

            this.distributorMasters = [];
            this.userService.distributors().subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'distributorMasters');
            });
        }
        this.trailupfrontpay.paymentType = null;
        this.trailupfrontpay.productName = null;
    }

    onItemSelect(item: any) {
        console.log(item);
        if (this.distributorMasters) {
            this.trailupfrontpay.distributorMaster = this.distributorMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
        }
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    cancel() {
        this._location.back();
    }

    onDateChange(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{4}-\d{2}-\d{2}/)) {
            this.trailupfrontpay.paymentDate = event.target.value;
            console.log(this.trailupfrontpay.paymentDate, '123');
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{4}-\d{2}-\d{2}/)) {
            this.trailupfrontpay.chequeDate = event.target.value;
            console.log(this.trailupfrontpay.chequeDate, '123');
        }
    }

    onSubmit() {
        this.trailupfrontpayService.save(this.trailupfrontpay).subscribe(data => {
            console.log('data');
            this.detail = data;
            console.log(this.detail, '123');
            if (this.detail === null) {
                this.router.navigate(['/trailupfrontpay']);
                swal('Payment added Successfully');
            }
            if (this.detail.error === '420') {
                swal({
                    title: this.detail.message,
                    text: 'Are you sure want to do Payment?',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes'
                }).then(result => {
                    if (result.value) {
                        console.log(result.value);
                        this.trailupfrontpayService.afterSave(this.trailupfrontpay).subscribe(data1 => {
                            console.log(data1);
                            swal('Payment added Successfully');
                            this.router.navigate(['/']);
                        });
                    }
                });
            } else {
                this.router.navigate(['/trailupfrontpay']);
                swal('Payment added Successfully');
            }
        });
    }
}
