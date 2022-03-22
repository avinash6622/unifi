import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RmService } from './../rm.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import { Rm } from '../rm.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-rm-add',
    templateUrl: './rm-add.component.html'
})
export class RmAddComponent implements OnInit {
    rm: Rm;
    subRMs: any[];
    locations: any[];
    dropdownSettings: {};

    constructor(
        private router: Router,
        private userService: UserService,
        private rmService: RmService,
        private locationService: LocationService,
        private _location: Location
    ) {
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
        this.rm = {};
        if (this.rm) {
            if (!this.rm.location) {
                this.rm.location = null;
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

    onSubmit() {
        if (this.rm) {
            if (this.rm.subRMS === null) {
                this.rm.subRMS = [];
            }
            if (this.rm.location === null) {
                this.rm.location = null;
            }
        }
        this.rmService.add(this.rm).subscribe(data => {
            if (data) {
                this.router.navigate(['/rm']);
                swal('Rm added Successfully');
            } else {
            }
        });
    }
}
