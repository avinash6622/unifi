import { Component, OnInit, EventEmitter } from '@angular/core';
import { FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubscriptionService } from 'app/subscription/subscription.service';
import { RedemptionService } from '../redemption.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal, UserService } from 'app/core';
import { Account } from '../../core/user/account.model';
import { Redemption } from '../redemption.model';
import { ClientCode } from '../redemption.model';
import { SeriesService } from 'app/series/series.service';
import { AifService } from 'app/clientmaster-cd/aif/aif.service';
import { DatePipe } from '@angular/common';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-redemption',
    templateUrl: './redemption.component.html'
})
export class RedemptionComponent implements OnInit {
    public invoiceForm: FormGroup;
    account: Account;
    redemption: Redemption;
    clientCode: ClientCode;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    seriesMasters: any[];
    aifClientMasters: any;
    dropdownSettings: {};
    dropdownSettings1: {};
    dropdownSettings2: {};
    seriesUnit: any;
    seriesUnit1: any;
    aifDistributor: any;
    disabledValue: boolean;
    disabledValue1: boolean;
    dummyData: any = [];
    apiValue: any;
    aifClientSeries: any;
    serieMaster: any;
    apiValue1: any;

    constructor(
        private _fb: FormBuilder,
        private router: Router,
        private subscriptionService: SubscriptionService,
        private redemptionService: RedemptionService,
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
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 20,
            allowSearchFilter: true
        };
        this.dropdownSettings1 = {
            singleSelection: true,
            idField: 'id',
            textField: 'clientCode',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 20,
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
            protfolioValue: '',
            closingUnits: '',
            noOfRedemptingUnit: '',
            costOfRedemptingUnit: ''
        };
        this.redemption = {};
        if (this.redemption) {
            if (!this.redemption.seriesMaster) {
                this.redemption.seriesMaster = null;
                console.log('null');
            }
            if (!this.redemption.subscriptionDate) {
                this.redemption.subscriptionDate = null;
            }
            if (!this.redemption.navAsOn) {
                this.redemption.navAsOn = null;
            }
        }
        this.seriesMasters = [];
        this.seriesService.series().subscribe(seriesMasters => {
            this.seriesMasters = seriesMasters;
            console.log(this.seriesMasters, 'seriesMasters');
        });
        // this.aifService.aif().subscribe(aifClientMasters => {
        //     this.aifClientMasters = aifClientMasters;
        //     console.log(this.aifClientMasters, 'aifClientMasters');
        // });
        this.redemption['clients'] = [
            {
                aifClientMaster: '',
                protfolioValue: '',
                closingUnits: '',
                noOfRedemptingUnit: '',
                costOfRedemptingUnit: '',
                subscriptionDate: this.redemption.subscriptionDate,
                seriesMaster: this.redemption.seriesMaster,
                navAsOn: this.redemption.navAsOn
            }
        ];
    }

    addFieldValue(index) {
        console.log(this.redemption.subscriptionDate, 'date');
        console.log('redemption', this.redemption.seriesMaster);
        console.log('redemption', this.redemption.subscriptionDate);
        console.log('Add index', index);
        console.log('Daataa', this.redemption.clients);
        this.redemption.clients.push({
            aifClientMaster: '',
            noOfUnits: '',
            protfolioValue: '',
            closingUnits: '',
            costOfRedemptingUnit: '',
            noOfRedemptingUnit: '',
            subscriptionDate: this.redemption.subscriptionDate,
            seriesMaster: this.redemption.seriesMaster,
            navAsOn: this.redemption.navAsOn
        });
        this.redemption.clients[index].subscriptionDate = this.redemption.subscriptionDate;
        this.redemption.clients[index].seriesMaster = this.redemption.seriesMaster;
        this.redemption.clients[index].navAsOn = this.redemption.navAsOn;
        this.redemption.clients[index].aifClientMaster = this.redemption.clients[index].aifClientMaster;
        this.redemption.clients[index].protfolioValue = this.redemption.clients[index].protfolioValue;
        this.redemption.clients[index].noOfRedemptingUnit = this.redemption.clients[index].noOfRedemptingUnit;
        this.redemption.clients[index].closingUnits = this.redemption.clients[index].closingUnits;
        this.redemption.clients[index].costOfRedemptingUnit = this.redemption.clients[index].costOfRedemptingUnit;
        console.log('Array::', this.redemption.clients[index], this.redemption.clients[index].navAsOn);
    }

    deleteFieldValue(index) {
        this.redemption.clients.splice(index, 1);
        console.log('post deletion::', this.redemption.clients);
    }

    onItemDeSelect(item: any) {
        this.redemption.navAsOn = '';
        this.disabledValue = false;
        // this.redemption.clients.forEach((value, index) => {
        //     console.log(value);
        //     value = {};
        this.redemption['clients'] = [];
        this.redemption.clients.push({
            aifClientMaster: '',
            noOfUnits: '',
            protfolioValue: '',
            closingUnits: '',
            costOfRedemptingUnit: '',
            noOfRedemptingUnit: '',
            subscriptionDate: this.redemption.subscriptionDate,
            seriesMaster: this.redemption.seriesMaster,
            navAsOn: this.redemption.navAsOn
        });
        // });
    }

    onItemSelect(item: any) {
        this.redemption['clients'] = [];
        this.redemption.clients.push({
            aifClientMaster: '',
            noOfUnits: '',
            protfolioValue: '',
            closingUnits: '',
            costOfRedemptingUnit: '',
            noOfRedemptingUnit: '',
            subscriptionDate: this.redemption.subscriptionDate,
            seriesMaster: this.redemption.seriesMaster,
            navAsOn: this.redemption.navAsOn
        });
        this.disabledValue = false;
        console.log(item);
        if (this.seriesMasters) {
            this.redemption.seriesMaster = this.seriesMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            const valdate = new DatePipe('en-US').transform(this.redemption.subscriptionDate, 'dd-MMM-yyyy');

            console.log(this.redemption.seriesMaster, '12345');
            this.redemptionService.seriesValue(this.redemption.seriesMaster, valdate).subscribe(data => {
                this.apiValue1 = data;
                console.log(this.apiValue1);
                if (this.apiValue1 === null) {
                    this.redemption.navAsOn = '';
                    this.disabledValue = true;
                } else {
                    // this.redemption.navAsOn = this.apiValue;
                    console.log(this.redemption.navAsOn);
                    this.redemption.navAsOn = this.apiValue1;
                    console.log('rwerwerwerwerwerwerwer', this.redemption.navAsOn);
                    this.redemption.clients[0].navAsOn = this.redemption.navAsOn;

                    console.log('redemptionnnnnnnnn', this.redemption.clients[0].navAsOn);
                    this.disabledValue = true;
                }
            });
            this.redemptionService.find(this.redemption.seriesMaster.id).subscribe(data => {
                console.log(data, 'listdata');
                this.aifClientMasters = data;
            });
        }
        console.log(this.redemption.navAsOn, 'navosan');

        this.redemption.clients[0].subscriptionDate = this.redemption.subscriptionDate;
        this.redemption.clients[0].seriesMaster = this.redemption.seriesMaster;
        this.redemption.clients[0].subscriptionDate = new DatePipe('en-US').transform(this.redemption.subscriptionDate, 'yyyy-MM-dd');
    }

    onItemSelect1(item: any, index: number) {
        this.disabledValue1 = false;
        console.log('index', index);
        console.log(item);
        if (this.aifClientMasters) {
            this.seriesUnit1 = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            console.log(this.seriesUnit1);
            // if (this.seriesUnit1) {
            //     this.redemption.clients[index].aifClientMaster = this.seriesUnit1;
            // }
            const requestBody = {
                aifClientMaster: this.redemption.clients[index].aifClientMaster[0],
                seriesMaster: this.redemption.seriesMaster
            };
            this.redemptionService.getValues(requestBody).subscribe(data => {
                this.apiValue = data;
                console.log(data, 'data');
                this.redemption.clients[index].closingUnits = this.apiValue.closingUnits;
                this.redemption.clients[index].protfolioValue = this.apiValue.protfolioValue;
            });
            this.redemption.clients[index].aifClientMaster1 = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            });
            if (this.seriesUnit1.distributorMaster === null) {
                this.redemption.clients[index].aifDist = '';
                this.redemption.clients[index].distributorMaster = null;
                this.disabledValue1 = true;
            } else {
                const data = this.seriesUnit1.distributorMaster;
                console.log(this.seriesUnit1.clientName, 'asdasdasd');
                this.redemption.clients[index].aifDist = data.distName;
                this.redemption.clients[index].distributorMaster = data;
                console.log(this.redemption.clients[index].aifClientMaster1, 'dada');
                console.log(this.redemption.clients[index].aifClientMaster);
            }
            console.log(this.redemption.clients[index].closingUnits, 'new');
        }
    }

    onValueChange(item: any, index: number) {
        this.redemption.clients[index].costOfRedemptingUnit = this.redemption.navAsOn * this.redemption.clients[index].noOfRedemptingUnit;
    }

    onItemSelect2(item: any, index: number) {
        this.disabledValue1 = false;
        console.log('index', index);
        console.log(item);
        if (this.aifClientMasters) {
            this.seriesUnit = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            })[0];
            const requestBody = {
                aifClientMaster: this.redemption.clients[index].aifClientMaster1[0],
                seriesMaster: this.redemption.seriesMaster
            };
            this.redemptionService.getValues(requestBody).subscribe(data => {
                this.apiValue = data;
                console.log(data, 'data');
                this.redemption.clients[index].closingUnits = this.apiValue.closingUnits;
                this.redemption.clients[index].protfolioValue = this.apiValue.protfolioValue;
            });
            console.log(this.redemption.clients[index].closingUnits, 'new');
            this.redemption.clients[index].aifClientMaster = this.aifClientMasters.filter(itm => {
                return itm.id === item.id;
            });
            console.log(this.seriesUnit);
            if (this.seriesUnit.distributorMaster === null) {
                this.redemption.clients[index].aifDist = '';
                this.redemption.clients[index].distributorMaster = null;
                this.disabledValue1 = true;
            } else {
                const data = this.seriesUnit.distributorMaster;
                console.log(this.seriesUnit.clientName, 'asdasdasd');
                this.redemption.clients[index].aifDist = data.distName;
                this.redemption.clients[index].distributorMaster = data;
                console.log(this.redemption.clients[index].aifClientMaster1, 'dada');
                console.log(this.redemption.clients[index].aifClientMaster);
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
        console.log(this.redemption.clients, 'use');
        const inputData = [];
        if (this.redemption.clients) {
            this.redemption.clients.forEach((value, index) => {
                console.log(value);
                const myObj = {};
                for (const i in value) {
                    // code...
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
        }
        this.redemptionService.save(inputData).subscribe(data => {
            if (data) {
                console.log('data');
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/redemption']);
            } else {
                console.log('new');
            }
            swal('redemption added Successfully');
            this.router.navigate(['/']);
        });
    }
}
