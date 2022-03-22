import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RmService } from './../rm.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import { Rm } from '../rm.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-rm-update',
    templateUrl: './rm-update.component.html',
    styleUrls: ['./rm-update.css']
})
export class RmUpdateComponent implements OnInit {
    rm: Rm;
    subRMs: any[];
    locations: any[];
    id: string;
    dropdownSettings: {};

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private userService: UserService,
        private rmService: RmService,
        private locationService: LocationService,
        private _location: Location
    ) {
        this.rm = {};
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'subName',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            itemsShowLimit: 3,
            allowSearchFilter: true
        };
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadRm(this.id);
        });
        if (this.rm) {
            if (!this.rm.location) {
                this.rm['location'] = {
                    id: null
                };
            }
        }
        this.subRMs = [];
        this.userService.subRMs().subscribe(subRMs => {
            this.subRMs = subRMs;
        });
        this.locations = [];
        this.locationService.location().subscribe(locations => {
            this.locations = locations;
        });
    }

    onItemSelect(item: any) {
        console.log(item);
    }
    onSelectAll(items: any) {
        console.log(items);
    }

    cancel() {
        this._location.back();
    }

    loadRm(id) {
        this.rmService.find(id).subscribe(rm => {
            this.rm = rm;
            if (this.rm) {
                if (!this.rm.location) {
                    this.rm['location'] = {
                        id: null
                    };
                }
            }
        });
    }

    onSubmit() {
        if (this.rm) {
            if (this.rm) {
                if (!this.rm.location.id) {
                    this.rm['location'] = null;
                }
            }
        }
        this.rmService.update(this.rm).subscribe(data => {
            if (data) {
                this.router.navigate(['/rm']);
                swal('Rm Updated Successfully');
            } else {
            }
        });
    }
}
