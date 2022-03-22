import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from './../role.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { RoleModel } from '../role-add/role-master.model';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-role',
    templateUrl: './role.component.html',
    styleUrls: ['role.css']
})
export class RoleComponent implements OnInit {
    account: Account;
    roles: RoleModel[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'clientCode';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private roleService: RoleService,
        private parseLinks: JhiParseLinks,
        private _location: Location,
        private principal: Principal
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.roleList(this.pageNumber);
    }

    backClicked() {
        this._location.back();
    }

    roleList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.roles = [];
        this.roleService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<RoleModel[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.roles = data;
        console.log(this.roles);
    }

    error(data) {
        console.log(data);
    }

    DeleteRole(role): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.roleService.delete(role.id).subscribe(data => {
                    this.roles = this.roles.filter(u => u !== role);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.roleList();
    }
}
