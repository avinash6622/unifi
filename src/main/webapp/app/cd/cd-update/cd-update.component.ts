import { Cd } from './../cd.model';
import { Component, OnInit, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DistributionTypeService } from './../../distribution-type/distribution-type.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { CdService } from '../cd.service';
import { ProductService } from 'app/product/product.service';

@Component({
    selector: 'jhi-cd-update',
    templateUrl: './cd-update.component.html',
    styleUrls: ['cd.css']
})
export class CdUpdateComponent implements OnInit {
    comdef: Cd;
    links: any;
    id: number;
    kotakShow = 0;
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
    enum_details = [
        { Id: 1, name: 'Management', IsSelected: null },
        { Id: 2, name: 'Performance', IsSelected: null },
        { Id: 3, name: 'Both', IsSelected: null }
    ];
    detail: any;

    constructor(
        private router: Router,
        private userService: UserService,
        private route: ActivatedRoute,
        private cdService: CdService,
        private locationService: LocationService,
        private productService: ProductService,
        private _location: Location
    ) {
        this.comdef = {};
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
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            console.log('route', this.id);
            this.loadcommission(this.id);
        });

        if (this.comdef) {
            if (!this.comdef.product) {
                this.comdef['product'] = {
                    id: null
                };
            }
            if (!this.comdef.distributorMasters) {
                this.comdef['distributorMasters'] = {
                    id: null
                };
            }
            if (!this.comdef.location) {
                this.comdef['location'] = {
                    id: null
                };
            }
            if (!this.comdef.relationshipManager) {
                this.comdef['relationshipManager'] = {
                    id: null
                };
            }
        }
    }

    cancel() {
        this._location.back();
    }

    onItemSelect(item: any) {
        console.log(item);
    }

    onSelectAll(items: any) {
        console.log(items);
        console.log(this.comdef.distributorMasters);
        // this.comdef.distributorMasters = (this.comdef.distributorMasters).concat(items);
        console.log(this.comdef.distributorMasters);
    }

    onDeSelect(event) {
        console.log(event);
        // this.cdService.productChange(this.comdef.product.id).subscribe(distributorMasters => {
        //     this.distributorMasters = distributorMasters;
        //     console.log(this.distributorMasters, 'masters');
        // });
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

    loadcommission(id) {
        console.log('Commission', id);
        this.cdService.find(id).subscribe(commission => {
            this.comdef = commission;
            let startDate = new Date(this.comdef.startYear);
            let endDate = new Date(this.comdef.endYear);

            this.comdef.startYear = startDate;
            this.comdef.endYear = endDate;

            if (this.comdef) {
                if (!this.comdef.product) {
                    this.comdef['product'] = {
                        id: null
                    };
                }
                if (!this.comdef.distributorMaster) {
                    this.comdef['distributorMaster'] = {
                        id: null
                    };
                }
                if (!this.comdef.location) {
                    this.comdef['location'] = {
                        id: null
                    };
                }
                if (!this.comdef.relationshipManager) {
                    this.comdef['relationshipManager'] = {
                        id: null
                    };
                }
            }
            this.distributorMasters = [];
            this.userService.distributors().subscribe(distributorMasters => {
                this.distributorMasters = distributorMasters;
                console.log(this.distributorMasters, 'distributorMasters');
            });
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
            console.log(this.comdef.product.id);
            console.log(this.comdef.commissionDefinitionOptionMaps);
            this.distributerValue = this.comdef.commissionDefinitionOptionMaps;
            console.log(this.distributerValue, '123');
            if (this.comdef.product.productName == 'BCAD' && this.comdef.bcadPMS === 0) {
                var kotaks = this.comdef.distributorMasters.filter(function(distributor) {
                    return distributor.distName === 'KOTAK Mahindra Bank ltd';
                });
                console.log('Kotak size', kotaks.length);
                if (kotaks.length == 1) {
                    console.log('Entering');
                    this.kotakShow = 1;
                }
            }
            // this.distributorMasters = [];
            // this.cdService.productChange(this.comdef.product.id).subscribe(distributorMasters => {
            //     this.distributorMasters = distributorMasters;
            //     console.log(this.distributorMasters, 'masters');
            // });
            // this.cdService.dropdownChange(this.comdef.product.id).subscribe(data => {
            //     if (data) {
            //         console.log('data', data);
            //         this.distributerValue = data;
            //         this.comdef.commissionDefinitionOptionMaps = [];
            //         console.log(this.comdef.commissionDefinitionOptionMaps, '1232');
            //         for (let i = 0; i < data['length']; i++) {
            //             this.comdef.commissionDefinitionOptionMaps.push({
            //                 distributorOption: data[i],
            //                 feeCalculation: ''
            //             });
            //         }
            //     }
            // });
        });
    }

    onRadioButton() {
        console.log(this.comdef.bcadPMS, '123');
        if (this.comdef.bcadPMS === 0) {
            this.distributorMasters = [];
            this.cdService.productChange(this.comdef.product.id).subscribe(distributorMasters => {
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
        this.distributorMasters = [];
        // this.cdService.productChange(product.id).subscribe(distributorMasters => {
        //     this.distributorMasters = distributorMasters;
        //     console.log(this.distributorMasters, 'masters');
        // });
        this.cdService.dropdownChange(product).subscribe(data => {
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
    }

    onSubmit() {
        if (this.comdef) {
            // if (this.comdef.distributorMaster.id === null || this.comdef.distributorMaster === null) {
            //     this.comdef.distributorMaster = null;
            // }
            if (this.comdef.distributorMasterOption.id === null || this.comdef.distributorMasterOption === null) {
                this.comdef.distributorMasterOption = null;
            }
            if (this.comdef.product.id === null || this.comdef.product === null) {
                this.comdef.product = null;
            }
            if (this.comdef.location === null || this.comdef.location.id === null) {
                this.comdef.location = null;
            }
            if (this.comdef.relationshipManager === null || this.comdef.relationshipManager.id === null) {
                this.comdef.relationshipManager = null;
            }
            this.cdService.update(this.comdef).subscribe(data => {
                this.detail = data;
                if (this.detail && this.detail.id) {
                    this.router.navigate(['cd']);
                    swal('Commission Updated Successfully');
                } else {
                    if (this.detail.error === '470') {
                        console.log(this.detail);
                        swal(this.detail.Message);
                    } else {
                        swal(this.detail.message);
                    }
                }
            });
        }
    }
}
