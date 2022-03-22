import { Component, OnInit } from '@angular/core';
import { UserManagement } from './adduser.service';
import { User, UserService } from 'app/core';

@Component({
    selector: 'jhi-adduser',
    templateUrl: './adduser.component.html',
    styleUrls: ['./adduser.component.css']
})
export class AdduserComponent implements OnInit {
    users = [];
    user = {
        username: '',
        firstname: '',
        lastname: '',
        password: '',
        email: '',
        distributorMaster: '',
        relationshipManager: '',
        subRM: ''
    };

    authorities = [];
    distributors = [];
    relations = [];
    subRMs = [];

    constructor(private userService: UserManagement, private userAuthorityService: UserService) {}

    ngOnInit() {
        this.userAuthorityService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.userAuthorityService.distributors().subscribe(distributors => {
            this.distributors = distributors;
        });
        this.userAuthorityService.relations().subscribe(relations => {
            this.relations = relations;
        });
        this.userAuthorityService.subRMs().subscribe(subRMs => {
            this.subRMs = subRMs;
        });
    }

    save(user) {
        if (this.user && this.user.distributorMaster) {
            this.user.distributorMaster = JSON.parse(this.user.distributorMaster);
        }
        if (this.user && this.user.relationshipManager) {
            this.user.relationshipManager = JSON.parse(this.user.relationshipManager);
        }
        if (this.user && this.user.subRM) {
            this.user.subRM = JSON.parse(this.user.subRM);
        }
        this.userService.save(this.user).subscribe(users => {
            this.users = users;
            console.log(this.users);
        });
    }
}
