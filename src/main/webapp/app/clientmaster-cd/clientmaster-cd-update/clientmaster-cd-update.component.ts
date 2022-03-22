import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Client } from '../clientmaster-cd.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ClientmasterService } from './../clientmaster-cd.service';
import swal from 'sweetalert2';
import { UserService } from 'app/core';
import { Location } from '@angular/common';
import { ProductService } from 'app/product/product.service';
import { DistributionService } from 'app/distribution-option/distribution-option.service';
@Component({
    selector: 'jhi-clientmaster-cd-update',
    templateUrl: './clientmaster-cd-update.component.html'
})
export class ClientmasterUpdateComponent implements OnInit {
    isSaving: boolean;
    clientMaster: Client;
    formSubmitted: boolean;
    distributorMasters: any[];
    relations: any[];
    subRMs: any[];
    distributorOptions: any[];
    products: any[];
    id: number;
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private userService: UserService,
        private distributionService: DistributionService,
        private productService: ProductService,
        private router: Router,
        private clientmasterService: ClientmasterService,
        private _location: Location
    ) {
        this.clientMaster = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadClientMaster(this.id);
        });
        if (this.clientMaster) {
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

    // onChange(product) {
    //     console.log(product);
    //     this.clientmasterService.dropdownChange(product.id).subscribe(data => {
    //         if (data) {
    //             console.log(data[0].optionName);
    //             // this.distributorOptions. = data;
    //         }
    //     });
    // }

    loadClientMaster(id) {
        this.clientmasterService.find(id).subscribe(clientMaster => {
            this.clientMaster = clientMaster;
            if (this.clientMaster) {
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
        });
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        if (this.clientMaster) {
            if (this.clientMaster.distributorMaster.id === null) {
                this.clientMaster.distributorMaster = null;
            }
            if (this.clientMaster.relationshipManager.id === null) {
                this.clientMaster.relationshipManager = null;
            }
            if (this.clientMaster.subRM.id === null) {
                this.clientMaster.subRM = null;
            }
            if (this.clientMaster.distributorOption.id === null) {
                this.clientMaster.distributorOption = null;
            }
            if (this.clientMaster.product.id === null) {
                this.clientMaster.product = null;
            }
        }
        this.clientmasterService.update(this.clientMaster).subscribe(data => {
            if (data) {
                this.router.navigate(['/Clientmaster']);
                swal('client updated Successfulliently');
            } else {
            }
        });
    }
}
