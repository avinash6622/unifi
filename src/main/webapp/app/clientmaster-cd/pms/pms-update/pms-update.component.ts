import { PmsService } from './../pms.service';
import { DistributionTypeService } from './../../../distribution-type/distribution-type.service';
import { DistributorMasterService } from './../../../distributor-master/distributor-master.service';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { Pms } from '../pms.model';

@Component({
    selector: 'jhi-pms-update',
    templateUrl: './pms-update.component.html'
})
export class PmsUpdateComponent implements OnInit {
    pm: Pms;
    links: any;
    distributorMasters: any[];
    relations: any[];
    subRMs: any[];
    id: number;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private distributionMasterService: DistributorMasterService,
        private userService: UserService,
        private _location: Location,
        private pmsService: PmsService
    ) {
        this.pm = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadPms(this.id);
        });
        if (this.pm) {
            if (!this.pm.relationshipManager) {
                this.pm['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.pm.distributorMaster) {
                this.pm['distributorMaster'] = {
                    id: null
                };
            }
            if (!this.pm.subRM) {
                this.pm['subRM'] = {
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
    }

    cancel() {
        this._location.back();
    }

    loadPms(id) {
        this.pmsService.find(id).subscribe(pm => {
            this.pm = pm;
            console.log(this.pm);
            if (this.pm) {
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
        });
    }

    onSubmit() {
        if (this.pm) {
            if (this.pm.distributorMaster.id === null) {
                this.pm.distributorMaster = null;
            }
            if (this.pm.relationshipManager.id === null) {
                this.pm.relationshipManager = null;
            }
            if (this.pm.subRM.id === null) {
                this.pm.subRM = null;
            }
        }
        this.pmsService.update(this.pm).subscribe(data => {
            if (data) {
                this.router.navigate(['/pmsclients']);
                swal('Pms Updated Successfully');
            } else {
            }
        });
    }
}
