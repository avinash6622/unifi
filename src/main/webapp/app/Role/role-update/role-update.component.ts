import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RoleService } from './../role.service';
import { DistributionTypeService } from './../../distribution-type/distribution-type.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import { RoleModel } from './../role-add/role-master.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-role-update',
    templateUrl: './role-update.component.html'
})
export class RoleUpdateComponent implements OnInit {
    links: any;
    role: RoleModel;
    id: number;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private roleService: RoleService,
        private userService: UserService,
        private distributiontypeService: DistributionTypeService,
        private locationService: LocationService,
        private _location: Location
    ) {
        this.role = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadRole(this.id);
        });
    }

    cancel() {
        this._location.back();
    }

    loadRole(id) {
        this.roleService.find(id).subscribe(role => {
            this.role = role;
            console.log(this.role);
        });
    }

    onSubmit() {
        this.roleService.update(this.role).subscribe(data => {
            if (data) {
                this.router.navigate(['/roles']);
                swal('Role Updated Successfully');
            } else {
            }
        });
    }
}
