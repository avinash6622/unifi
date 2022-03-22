import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Client } from '../clientmaster-cd.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ClientmasterService } from './../clientmaster-cd.service';
import swal from 'sweetalert2';
import { UserService } from 'app/core';
import { ProductService } from 'app/product/product.service';
import { DistributionService } from 'app/distribution-option/distribution-option.service';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-clientmaster-cd-add',
    templateUrl: './clientmaster-cd-add.component.html'
})
export class ClientmasterAddComponent implements OnInit {
    isSaving: boolean;
    clientMaster: Client;
    formSubmitted: boolean;
    distributorMasters: any[];
    relations: any[];
    subRMs: any[];
    distributorOptions: any[];
    products: any[];
    onFormSubmit = new EventEmitter();
    distributerValue: any;

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private distributionService: DistributionService,
        private productService: ProductService,
        private router: Router,
        private clientmasterService: ClientmasterService,
        private _location: Location
    ) {}

    ngOnInit() {
        this.clientMaster = {};
        if (this.clientMaster) {
            if (!this.clientMaster.distributorMaster) {
                this.clientMaster.distributorMaster = null;
                console.log('null');
            }
            if (!this.clientMaster.relationshipManager) {
                this.clientMaster.relationshipManager = null;
            }
            if (!this.clientMaster.subRM) {
                this.clientMaster.subRM = null;
            }
            if (!this.clientMaster.distributorOption) {
                this.clientMaster.distributorOption = null;
            }
            if (!this.clientMaster.product) {
                this.clientMaster.product = null;
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
            this.subRMs = [];
            this.userService.subRMs().subscribe(subRMs => {
                this.subRMs = subRMs;
            });
            this.distributorOptions = [];
            this.distributionService.distributorOptions().subscribe(distributorOptions => {
                this.distributorOptions = distributorOptions;
            });
            this.products = [];
            this.productService.product().subscribe(products => {
                this.products = products;
            });
        }
    }

    cancel() {
        this._location.back();
    }

    onChange(product) {
        console.log(product);
        this.clientmasterService.dropdownChange(product.id).subscribe(data => {
            if (data) {
                console.log(data[0].optionName);
                this.distributerValue = data;
            }
        });
    }

    onSubmit() {
        if (!this.clientMaster) {
            if (!this.clientMaster.distributorMaster) {
                this.clientMaster['distributorMaster'] = {
                    id: null
                };
            }
            if (!this.clientMaster.relationshipManager) {
                this.clientMaster['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.clientMaster.subRM) {
                this.clientMaster['subRM'] = {
                    id: null
                };
            }
            if (!this.clientMaster.distributorOption) {
                this.clientMaster['distributorOption'] = {
                    id: null
                };
            }
            if (!this.clientMaster.product) {
                this.clientMaster['product'] = {
                    id: null
                };
            }
        }
        this.clientmasterService.add(this.clientMaster).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/Clientmaster']);
                swal('client added Successfulliently');
            } else {
            }
        });
    }
}
