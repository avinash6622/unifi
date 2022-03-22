import { Component, OnInit } from '@angular/core';
import { AddRole } from './addrole.service';

@Component({
    selector: 'jhi-addrole',
    templateUrl: './addrole.component.html',
    styleUrls: ['./addrole.component.css']
})
export class AddroleComponent implements OnInit {
    role = {
        roleName: ''
    };
    constructor(private roleservice: AddRole) {
        console.log('works..!');
    }
    ngOnInit() {}

    save(data) {
        console.log(data);

        this.roleservice.save(this.role).subscribe(clientmanagement => {});
        console.log('normal save works...');
    }
}
