import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from './../role.service';
import { DistributionTypeService } from './../../distribution-type/distribution-type.service';
import { UserService } from 'app/core';
import { LocationService } from 'app/location/location.service';
import swal from 'sweetalert2';
import { RoleModel } from './role-master.model';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-role-add',
    templateUrl: './role-add.component.html'
})
export class RoleAddComponent implements OnInit {
    links: any;
    role: RoleModel;

    constructor(
        private router: Router,
        private roleService: RoleService,
        private userService: UserService,
        private distributiontypeService: DistributionTypeService,
        private locationService: LocationService,
        private _location: Location
    ) {}

    ngOnInit() {
        this.role = {
            roleName: '',
            roleNameMasters: [
                {
                    roleName: 'Distributor',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Investment',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'RM',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'SubRM',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Role',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'DistType',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'DistributorOption',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Location',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Product',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Series',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'ClientMaster',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'CommissionDefinition',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'FileUpload',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Payment',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'ManagementFee',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'ClientfeeCommission',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                },
                {
                    roleName: 'Report Generation',
                    roleCreate: false
                },
                {
                    roleName: 'User',
                    roleCreate: false,
                    roleEdit: false,
                    roleView: false,
                    roleDelete: false
                }
            ]
        };
    }

    checkIfAllSelected() {
        this.role = {
            roleName: '',
            roleNameMasters: [
                {
                    roleName: 'Distributor',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Investment',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'RM',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'SubRM',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Role',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'DistType',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'DistributorOption',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Location',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Product',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Series',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'ClientMaster',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'CommissionDefinition',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'FileUpload',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Payment',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'ManagementFee',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'ClientfeeCommission',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                },
                {
                    roleName: 'Report Generation',
                    roleCreate: true
                },
                {
                    roleName: 'User',
                    roleCreate: true,
                    roleEdit: true,
                    roleView: true,
                    roleDelete: true
                }
            ]
        };
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        console.log(this.role);
        this.roleService.add(this.role).subscribe(data => {
            if (data) {
                this.router.navigate(['/roles']);
                swal('Role added Successfully');
            } else {
                console.log('error');
            }
        });
    }
}
