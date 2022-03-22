import { AifService } from './../aif.service';
import { Aif } from './../aif.model';
import { DistributionTypeService } from './../../../distribution-type/distribution-type.service';
import { DistributorMasterService } from './../../../distributor-master/distributor-master.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'app/core';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-aif-add',
    templateUrl: './aif-add.component.html'
})
export class AifAddComponent implements OnInit {
    aif: Aif;
    links: any;
    distributorMasters: any[];
    relations: any[];
    subRMs: any[];

    constructor(
        private router: Router,
        private distributionMasterService: DistributorMasterService,
        private userService: UserService,
        private _location: Location,
        private aifService: AifService
    ) {
        this.aif = {};
    }

    ngOnInit() {
        if (this.aif) {
            if (!this.aif.relationshipManager) {
                this.aif.relationshipManager = null;
                console.log('null');
            }
            if (!this.aif.distributorMaster) {
                this.aif.distributorMaster = null;
            }
            if (!this.aif.subRM) {
                this.aif.subRM = null;
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
        if (!this.aif) {
            if (!this.aif.distributorMaster) {
                this.aif['distributorMaster'] = {
                    id: null
                };
            }
            if (!this.aif.relationshipManager) {
                this.aif['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.aif.subRM) {
                this.aif['subRM'] = {
                    id: null
                };
            }
        }
        this.aifService.add(this.aif).subscribe(data => {
            if (data) {
                this.router.navigate(['/aifclients']);
                swal('aifs added Successfully');
            } else {
            }
        });
    }
}
