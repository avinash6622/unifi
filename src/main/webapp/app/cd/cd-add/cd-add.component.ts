import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import { Cd, Option } from '../cd.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { ProductService } from 'app/product/product.service';
import { CdService } from '../cd.service';
import { DistributionService } from '../../distribution-option/distribution-option.service';

@Component({
    selector: 'jhi-cd-add',
    templateUrl: './cd-add.component.html'
})
export class CdAddComponent implements OnInit {
    distType: any;
    comdef: Cd;
    links: any;
    modalRef: NgbModalRef;
    distributorMasters: any;
    products: any[];
    relations: any[];
    locations: any[];
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    dropdownSettings: {};
    dropdownSettings1: {};
    distributerValue: any;
    distributorOptions: any[];
    radioType: any;
    kotakShow = 0;
    enum_details = [
        { Id: 1, name: 'Management', IsSelected: null },
        { Id: 2, name: 'Performance', IsSelected: null },
        { Id: 3, name: 'Both', IsSelected: null }
    ];

    constructor(
        private router: Router,
        private userService: UserService,
        private cdService: CdService,
        private locationService: LocationService,
        private productService: ProductService,
        private _location: Location,
        private distributionService: DistributionService
    ) {
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'distName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 180,
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
    }

    ngOnInit() {
        this.kotakShow = 0;
        this.comdef = {
            commissionDefinitionOptionMaps: []
        };

        if (this.comdef) {
            if (!this.comdef.distributorMaster) {
                this.comdef.distributorMaster = null;
            }
            if (!this.comdef.relationshipManager) {
                this.comdef.relationshipManager = null;
            }
            if (!this.comdef.location) {
                this.comdef.location = null;
            }
            if (!this.comdef.product) {
                this.comdef.product = null;
            }
            if (!this.comdef.distributorOption) {
                this.comdef.distributorOption = null;
            }
        }
        // this.relations = [];
        // this.userService.relations().subscribe(relations => {
        //     this.relations = relations;
        // });
        this.locations = [];
        this.locationService.location().subscribe(locations => {
            this.locations = locations;
        });
        this.products = [];
        this.productService.product().subscribe(products => {
            this.products = products;
        });
        // this.distributorMasters = [];
        // this.userService.distributorsfilter().subscribe(distributorMasters => {
        //     this.distributorMasters = distributorMasters;
        //     console.log(this.distributorMasters, 'masters');
        // });
        // this.distributorOptions = [];
        // this.distributionService.distributorOptions().subscribe(distributorOptions => {
        //     this.distributorOptions = distributorOptions;
        // });
    }

    onRadioButton() {
        console.log(this.comdef.bcadPMS, '123');
        console.log(this.comdef.pmsInvest, '234');
        if (this.comdef.bcadPMS === 0) {
            this.distributorMasters = [];
            this.cdService.productChange(this.comdef.product.id).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters');
            });
        }

        if (this.comdef.bcadPMS === 1) {
            console.log(this.comdef.pmsInvest);
            this.distributorMasters = [];
            this.cdService.optionProductChange(this.comdef.product.id, this.comdef.pmsInvest).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters');
            });
        }

        if (this.comdef.pmsInvest === 0) {
            console.log('data enters');
            this.distributorMasters = [];
            this.cdService.pmsChange(this.comdef.product.id, this.comdef.pmsInvest).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters addition');
            });
        }
        if (this.comdef.pmsInvest === 1) {
            console.log('data enters1');
            this.distributorMasters = [];
            this.cdService.pmsChange(this.comdef.product.id, this.comdef.pmsInvest).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters addition');
            });
        }
    }
    onRadioButton1() {
        if (this.comdef.bcadPMS === 1) {
            console.log(this.comdef.pmsInvest);
            this.distributorMasters = [];
            this.cdService.optionProductChange(this.comdef.product.id, this.comdef.pmsInvest).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters');
            });
        }
    }

    onChange(product) {
        console.log(this.comdef, 'new');
        console.log(product);
        this.cdService.dropdownChange(product.id).subscribe(data => {
            if (data) {
                console.log('data', data);
                this.distributerValue = data;
                this.comdef.commissionDefinitionOptionMaps = [];
                console.log(this.comdef.commissionDefinitionOptionMaps, '1232');
                for (let i = 0; i < data['length']; i++) {
                    this.comdef.commissionDefinitionOptionMaps.push({
                        distributorOption: data[i],
                        feeCalculation: ''
                    });
                }
            }
        });
        if (this.comdef.product.productName !== 'BCAD') {
            this.distributorMasters = [];
            this.cdService.productChange(this.comdef.product.id).subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'masters');
            });
        }
    }

    onSubmit() {
        console.log(this.comdef);
        //if()
        this.cdService.add(this.comdef).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['cd']);
                swal('CommissionDefinition added Successfully');
            } else {
            }
        });
    }

    cancel() {
        this._location.back();
    }
    onDateChange(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.comdef.startYear = event.target.value;
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.comdef.endYear = event.target.value;
        }
    }
    onItemSelect(item: any) {
        if (item.distName == 'KOTAK Mahindra Bank ltd') {
            this.kotakShow = 1;
            console.log('--------->Kotak');
        }
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
        var kotaks = items.filter(function(distributor) {
            return distributor.distName === 'KOTAK Mahindra Bank ltd';
        });
        console.log('Kotak size', kotaks.length);
        if (kotaks.length == 1) {
            console.log('Entering');
            this.kotakShow = 1;
        }
    }
}
