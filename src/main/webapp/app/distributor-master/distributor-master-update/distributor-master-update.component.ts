import { DistributorMaster } from './../distributor-master.model';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DistributorMasterService } from './../distributor-master.service';
import { DistributionTypeService } from './../../distribution-type/distribution-type.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-distributor-master-update',
    templateUrl: './distributor-master-update.component.html'
})
export class DistributorMasterUpdateComponent implements OnInit {
    distributor: DistributorMaster;
    links: any;
    distributorTypes: any[];
    relations: any[];
    locations: any[];
    id: number;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private distributionMasterService: DistributorMasterService,
        private userService: UserService,
        private distributiontypeService: DistributionTypeService,
        private locationService: LocationService,
        private _location: Location
    ) {
        this.distributor = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadDistributor(this.id);
        });
        if (this.distributor) {
            if (!this.distributor.distributorType) {
                this.distributor['distributorType'] = {
                    id: null
                };
            }
            if (!this.distributor.relationshipManager) {
                this.distributor['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.distributor.location) {
                this.distributor['location'] = {
                    id: null
                };
            }
        }

        this.distributorTypes = [];
        this.distributiontypeService.distributorType().subscribe(distributorTypes => {
            this.distributorTypes = distributorTypes;
        });
        this.relations = [];
        this.userService.relations().subscribe(relations => {
            this.relations = relations;
        });
        this.locations = [];
        this.locationService.location().subscribe(locations => {
            this.locations = locations;
        });
    }

    cancel() {
        this._location.back();
    }

    loadDistributor(id) {
        this.distributionMasterService.find(id).subscribe(distributor => {
            this.distributor = distributor;
            console.log(this.distributor);
            if (this.distributor) {
                if (!this.distributor.distributorType) {
                    this.distributor['distributorType'] = {
                        id: null
                    };
                }
                if (!this.distributor.relationshipManager) {
                    this.distributor['relationshipManager'] = {
                        id: null
                    };
                }
                if (!this.distributor.location) {
                    this.distributor['location'] = {
                        id: null
                    };
                }
            }
        });
    }

    onSubmit() {
        if (this.distributor) {
            if (this.distributor.distributorType.id === null) {
                this.distributor.distributorType = null;
            }
            if (this.distributor.relationshipManager.id === null) {
                this.distributor.relationshipManager = null;
            }
            if (this.distributor.location.id === null) {
                this.distributor.location = null;
            }
        }
        this.distributionMasterService.update(this.distributor).subscribe(data => {
            if (data) {
                this.router.navigate(['/distributor-master']);
                swal('Distribution Master Updated Successfully');
            } else {
            }
        });
    }
}
