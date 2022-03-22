import { Component, OnInit, EventEmitter } from '@angular/core';
import { FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubscriptionService } from '../subscription.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from '../../core/user/account.model';
import { Subscription } from '../subscription.model';
import { ClientCode } from '../subscription.model';
import { SeriesService } from 'app/series/series.service';
import { AifService } from 'app/clientmaster-cd/aif/aif.service';
import { DatePipe } from '@angular/common';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-subscription',
    templateUrl: './subscription.component.html'
})
export class SubscriptionComponent implements OnInit {
    public invoiceForm: FormGroup;
    account: Account;
    subscription: Subscription;
    clientCode: ClientCode;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    seriesMasters: any[];
    aifClientMasters: any[];
    dropdownSettings: {};
    dropdownSettings1: {};
    dropdownSettings2: {};
    seriesUnit: any;
    seriesUnit1: any;
    aifDistributor: any;
    disabledValue: boolean;
    disabledValue1: boolean;
    dummyData: any = [];
    serieMaster: any;
    aifDist: any;

    constructor(
        private _fb: FormBuilder,
        private router: Router,
        private subscriptionService: SubscriptionService,
        private principal: Principal,
        private userService: UserService,
        private seriesService: SeriesService,
        private aifService: AifService,
        private _location: Location
    ) {
        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'seriesCode',
            itemsShowLimit: 20,
            allowSearchFilter: true
        };
        this.dropdownSettings1 = {
            singleSelection: true,
            textField: 'clientCode',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 100,
            allowSearchFilter: true
        };
        this.dropdownSettings2 = {
            singleSelection: true,
            idField: 'id',
            textField: 'clientName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 20,
            allowSearchFilter: true
        };
    }

    ngOnInit() {
        this.dummyData = {
            subscriptionDate: '',
            seriesMaster: '',
            navAsOn: ''
        };
        this.dummyData['clients'] = {
            aifClientMaster: '',
            noOfUnits: '',
            protfolioValue: ''
        };
        this.subscription = {};
        if (this.subscription) {
            if (!this.subscription.seriesMaster) {
                this.subscription.seriesMaster = null;
                console.log('null');
            }
            if (!this.subscription.subscriptionDate) {
                this.subscription.subscriptionDate = null;
            }
            if (!this.subscription.navAsOn) {
                this.subscription.navAsOn = null;
            }
        }
        this.seriesMasters = [];
        this.seriesService.series().subscribe(seriesMasters => {
            this.seriesMasters = seriesMasters;
            console.log(this.seriesMasters, 'seriesMasters');
        });
        this.aifClientMasters = [];
        this.aifService.aif().subscribe(aifClientMasters => {
            this.aifClientMasters = aifClientMasters;
            console.log(this.aifClientMasters, 'aifClientMasters');
        });
        this.subscription['clients'] = [
            {
                aifClientMaster: '',
                noOfUnits: '',
                protfolioValue: '',
                subscriptionDate: this.subscription.subscriptionDate,
                seriesMaster: this.subscription.seriesMaster,
                navAsOn: this.subscription.navAsOn
            }
        ];
    }

    addFieldValue(index) {
        console.log(this.subscription.subscriptionDate, 'date');
        console.log('subscription', this.subscription.seriesMaster);
        console.log('subscription', this.subscription.subscriptionDate);
        console.log('Add index', index);
        console.log('Daataa', this.subscription.clients);
        this.subscription.clients.push({
            aifClientMaster: '',
            noOfUnits: '',
            protfolioValue: '',
            subscriptionDate: this.subscription.subscriptionDate,
            seriesMaster: this.subscription.seriesMaster,
            navAsOn: this.subscription.navAsOn
        });
        this.subscription.clients[index].subscriptionDate = this.subscription.subscriptionDate;
        this.subscription.clients[index].seriesMaster = this.subscription.seriesMaster;
        this.subscription.clients[index].navAsOn = this.subscription.navAsOn;
        this.subscription.clients[index].aifClientMaster = this.subscription.clients[index].aifClientMaster;
        this.subscription.clients[index].noOfUnits = this.subscription.clients[index].noOfUnits;
        this.subscription.clients[index].protfolioValue = this.subscription.clients[index].protfolioValue;
        console.log('Array::', this.subscription.clients[index]);
    }

    deleteFieldValue(index) {
        console.log('delete index', index);
        this.subscription.clients.splice(index, 1);
        console.log('post deletion::', this.subscription.clients);
    }

    onValueChange(item: any, index: number) {
        this.subscription.clients[index].protfolioValue = this.subscription.initPerCost * this.subscription.clients[index].noOfUnits;
    }

    onItemDeSelect(item: any) {
        this.subscription.navAsOn = '';
        this.disabledValue = false;
    }

    onItemSelect(item: any) {
        this.disabledValue = false;
        console.log(item);
        if (this.seriesMasters) {
            this.subscription.seriesMaster = this.seriesMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            this.subscription.initPerCost = this.subscription.seriesMaster.initPerCost;
            if (this.subscription.seriesMaster.initPerCost === null) {
                this.subscription.navAsOn = '';
                this.disabledValue = true;
            } else {
                this.subscription.navAsOn = this.subscription.seriesMaster.initPerCost;
                this.disabledValue = true;
            }
        }
        this.subscription.subscriptionDate = new DatePipe('en-US').transform(this.subscription.subscriptionDate, 'yyyy-MM-dd');
        this.subscription.clients[0].subscriptionDate = this.subscription.subscriptionDate;
        this.subscription.clients[0].seriesMaster = this.subscription.seriesMaster;
        this.subscription.clients[0].navAsOn = this.subscription.initPerCost;
    }

    onItemSelect1(item: any, index: number) {
        this.disabledValue1 = false;
        console.log('index', index);
        console.log(item);
        console.log(this.subscription.clients[index].aifClientMaster, '1');
        if (this.aifClientMasters) {
            this.seriesUnit1 = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            console.log(this.seriesUnit1);
            if (this.seriesUnit1) {
                // this.subscription.clients[index].aifClientMaster = this.seriesUnit1;
            }
            if (this.seriesUnit1.distributorMaster === null) {
                this.clientCode.aifDist = '';
                this.subscription.clients[index].distributorMaster = null;
                this.disabledValue1 = true;
            } else {
                const data = this.seriesUnit1.distributorMaster;
                console.log(this.seriesUnit1.clientName, 'asdasdasd');
                this.subscription.clients[index].aifDist = data.distName;
                this.subscription.clients[index].distributorMaster = data;
                this.subscription.clients[index].aifClientMaster1 = this.aifClientMasters.filter(itm => {
                    return itm.id === item.id;
                });
                console.log(this.subscription.clients[index].aifClientMaster1, 'dada');
                console.log(this.subscription.clients[index].aifClientMaster);
            }
        }
    }

    onItemSelect2(item: any, index: number) {
        this.disabledValue1 = false;
        console.log('index', index);
        console.log(item);
        if (this.aifClientMasters) {
            this.seriesUnit = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            console.log(this.seriesUnit);
            if (this.seriesUnit.distributorMaster === null) {
                this.clientCode.aifDist = '';
                this.subscription.clients[index].distributorMaster = null;
                this.disabledValue1 = true;
            } else {
                const data = this.seriesUnit.distributorMaster;
                console.log(this.seriesUnit.clientName, 'asdasdasd');
                this.subscription.clients[index].aifDist = data.distName;
                this.subscription.clients[index].distributorMaster = data;
                this.subscription.clients[index].aifClientMaster = this.aifClientMasters.filter(itm => {
                    return itm.id === item.id;
                });
                // this.subscription.clients[index].aifClientMaster1 = this.seriesUnit.clientName;
            }
        }
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        console.log(this.subscription.clients, 'use');
        const inputData = [];
        if (this.subscription.clients) {
            this.subscription.clients.forEach((value, index) => {
                console.log(value);
                const myObj = {};
                for (const i in value) {
                    if (value.hasOwnProperty(i)) {
                        if (i === 'aifClientMaster') {
                            myObj['aifClientMaster'] = value['aifClientMaster'][0];
                        } else {
                            myObj[i] = value[i];
                        }
                    }
                }
                inputData.push(myObj);
            });
            console.log('inputData', inputData, inputData.length);
        }
        // if(inputData) {
        //     inputData.forEach
        // }
        this.subscriptionService.save(inputData).subscribe(data => {
            if (data) {
                console.log('data');
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/']);
            } else {
                console.log('new');
            }
            swal('Subscription added Successfully');
            this.router.navigate(['/']);
        });
    }
}
