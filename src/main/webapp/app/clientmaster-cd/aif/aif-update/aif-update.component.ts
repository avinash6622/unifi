import { AifService } from './../aif.service';
import { DistributorMasterService } from './../../../distributor-master/distributor-master.service';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from 'app/core';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { Aif } from './../aif.model';

@Component({
    selector: 'jhi-aif-update',
    templateUrl: './aif-update.component.html'
})
export class AifUpdateComponent implements OnInit {
    aif: Aif;
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
        private aifService: AifService
    ) {
        this.aif = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadAif(this.id);
        });
        if (this.aif) {
            if (!this.aif.relationshipManager) {
                this.aif['relationshipManager'] = {
                    id: null
                };
            }
            if (!this.aif.distributorMaster) {
                this.aif['distributorMaster'] = {
                    id: null
                };
            }
            if (!this.aif.subRM) {
                this.aif['subRM'] = {
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

    loadAif(id) {
        this.aifService.find(id).subscribe(aif => {
            this.aif = aif;
            console.log(this.aif);
            if (this.aif) {
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
        });
    }

    onSubmit() {
        if (this.aif) {
            if (this.aif.distributorMaster.id === null) {
                this.aif.distributorMaster = null;
            }
            if (this.aif.relationshipManager.id === null) {
                this.aif.relationshipManager = null;
            }
            if (this.aif.subRM.id === null) {
                this.aif.subRM = null;
            }
        }
        this.aifService.update(this.aif).subscribe(data => {
            if (data) {
                this.router.navigate(['/aifclients']);
                swal('Aif Updated Successfully');
            } else {
            }
        });
    }
}
