import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { User, UserService } from 'app/core';

@Component({
    selector: 'jhi-user-mgmt-update',
    templateUrl: './user-management-update.component.html'
})
export class UserMgmtUpdateComponent implements OnInit {
    user: User;
    languages: any[];
    authorities: any[];
    distributors: any[];
    relations: any[];
    subRMs: any[];
    roles: any[];
    isSaving: boolean;
    disabledValue: boolean;
    disabledValue1: boolean;
    disabledValue2: boolean;

    constructor(private userService: UserService, private route: ActivatedRoute, private router: Router) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
            console.log(this.user);
            if (this.user) {
                if (!this.user.distributorMaster) {
                    this.user['distributorMaster'] = {
                        id: null
                    };
                }
                if (!this.user.relationshipManager) {
                    this.user['relationshipManager'] = {
                        id: null
                    };
                }
                if (!this.user.subRM) {
                    this.user['subRM'] = {
                        id: null
                    };
                }
                if (!this.user.role) {
                    this.user['role'] = {
                        id: null
                    };
                }
            }
        });
        if (this.user.distributorMaster.id !== null) {
            this.disabledValue = true;
        }
        if (this.user.relationshipManager.id !== null) {
            this.disabledValue1 = true;
        }
        if (this.user.relationshipManager.id !== null) {
            this.disabledValue2 = true;
        }
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.distributors = [];
        this.userService.distributors().subscribe(distributors => {
            this.distributors = distributors;
        });
        this.relations = [];
        this.userService.relations().subscribe(relations => {
            this.relations = relations;
        });
        this.subRMs = [];
        this.userService.subRMs().subscribe(subRMs => {
            this.subRMs = subRMs;
        });
        this.roles = [];
        this.userService.roles().subscribe(roles => {
            this.roles = roles;
        });
    }

    onItemSelect(item: any) {
        console.log(this.user.distributorMaster.id, '123');
        this.disabledValue = false;
        console.log('select', item, item.target.value);
        if (this.user.distributorMaster.id === null) {
            this.disabledValue = false;
            console.log('123');
        } else {
            this.disabledValue = true;
        }
    }

    onItemSelect1(item: any) {
        console.log(this.user.relationshipManager.id, '123');
        this.disabledValue1 = false;
        console.log('select', item, item.target.value);
        if (this.user.relationshipManager.id === null) {
            this.disabledValue1 = false;
            console.log('123');
        } else {
            this.disabledValue1 = true;
        }
    }

    onItemSelect2(item: any) {
        console.log(this.user.subRM.id, '123');
        this.disabledValue2 = false;
        console.log('select', item, item.target.value);
        if (this.user.subRM.id === null) {
            this.disabledValue2 = false;
            console.log('123');
        } else {
            this.disabledValue2 = true;
        }
    }

    previousState() {
        this.router.navigate(['/admin/user-management']);
    }

    save() {
        this.isSaving = true;
        if (!this.user) {
            if (!this.user.distributorMaster) {
                this.user.distributorMaster = null;
                console.log('null');
            }
            if (!this.user.relationshipManager) {
                this.user.relationshipManager = null;
            }
            if (!this.user.subRM) {
                this.user.subRM = null;
            }
            if (!this.user.role) {
                this.user.role = null;
            }
        }

        if (this.user.id !== null) {
            console.log('user creation after', this.user);
            this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.user.langKey = 'en';
            console.log('user creation before', this.user);
            this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
