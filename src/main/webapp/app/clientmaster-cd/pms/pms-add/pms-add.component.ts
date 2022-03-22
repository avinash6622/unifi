import { PmsService } from './../pms.service';
import { DistributionTypeService } from './../../../distribution-type/distribution-type.service';
import { DistributorMasterService } from './../../../distributor-master/distributor-master.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { Pms } from './../pms.model';

@Component({
    selector: 'jhi-pms-add',
    templateUrl: './pms-add.component.html'
})
export class PmsAddComponent implements OnInit {
    pm: Pms;
    links: any;
    distributorMasters: any[];
    relations: any[];
    subRMs: any[];

    constructor(
        private router: Router,
        private distributionMasterService: DistributorMasterService,
        private userService: UserService,
        private _location: Location,
        private pmsService: PmsService
    ) {}

    ngOnInit() {
        this.pm = {};
        if (this.pm) {
            if (!this.pm.relationshipManager) {
                this.pm.relationshipManager = null;
                console.log('null');
            }
            if (!this.pm.distributorMaster) {
                this.pm.distributorMaster = null;
            }
            if (!this.pm.subRM) {
                this.pm.subRM = null;
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
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        if (!this.pm) {
            if (!this.pm.distributorMaster) {
                this.pm['distributorMaster'] = {
                    id: null
                };
            }
            if (!this.pm.relationshipManager) {
                this.pm['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.pm.subRM) {
                this.pm['subRM'] = {
                    id: null
                };
            }
        }
        this.pmsService.add(this.pm).subscribe(data => {
            if (data) {
                this.router.navigate(['/pmsclients']);
                swal('Pms added Successfully');
            } else {
            }
        });
    }
}
