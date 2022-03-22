import { Component, OnInit } from '@angular/core';
import { UserManagement } from './edituser.service';
import { UserService } from 'app/core';

@Component({
    selector: 'jhi-edituser',
    templateUrl: './edituser.component.html',
    styleUrls: ['./edituser.component.css']
})
export class EdituserComponent implements OnInit {
    users = [];
    user = {
        login: '',
        firstName: '',
        lastName: '',
        email: '',
        distributorMaster: '',
        relationshipManager: '',
        subRM: ''
    };

    distributors = [];

    constructor(private userService: UserManagement, private userAuthorityService: UserService) {}

    ngOnInit() {
        this.userService.search().subscribe(users => {
            this.userService = users;
        });
        this.userAuthorityService.distributors().subscribe(distributors => {
            this.distributors = distributors;
        });
    }
    edit(user) {
        this.user = user;
    }
    clear() {
        this.user = {
            login: '',
            firstName: '',
            lastName: '',
            email: '',
            distributorMaster: '',
            relationshipManager: '',
            subRM: ''
        };
    }
    update(user) {}
    // search() {
    //     this.userService.search().subscribe(users=> {
    //         this.userService=users;
    //     });
    // }
}
