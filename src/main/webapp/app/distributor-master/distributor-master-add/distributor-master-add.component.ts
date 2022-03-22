import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DistributorMasterService } from './../distributor-master.service';
import { DistributionTypeService } from './../../distribution-type/distribution-type.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import { DistributorMaster } from '../distributor-master.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-distributor-master-add',
    templateUrl: './distributor-master-add.component.html'
})
export class DistributorMasterAddComponent implements OnInit {
    distributor: DistributorMaster;
    links: any;
    distributorTypes: any[];
    relations: any[];
    locations: any[];

    constructor(
        private router: Router,
        private distributionMasterService: DistributorMasterService,
        private userService: UserService,
        private distributiontypeService: DistributionTypeService,
        private locationService: LocationService,
        private _location: Location
    ) {}

    ngOnInit() {
        this.distributor = {};

        if (this.distributor) {
            if (!this.distributor.distributorType) {
                this.distributor.distributorType = null;
                console.log('null');
            }
            if (!this.distributor.relationshipManager) {
                this.distributor.relationshipManager = null;
            }
            if (!this.distributor.location) {
                this.distributor.location = null;
            }
        }
        this.distributorTypes = [];
        this.distributiontypeService.distributorType().subscribe(distributorTypes => {
            this.distributorTypes = distributorTypes;
            console.log(this.distributorTypes, 'distributorTypes');
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

    onSubmit() {
        if (!this.distributor) {
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
        console.log(this.distributor, 'new');
        this.distributionMasterService.add(this.distributor).subscribe(data => {
            if (data) {
                this.router.navigate(['/distributor-master']);
                swal('Distribution Master added Successfully');
            } else {
            }
        });
    }
}
