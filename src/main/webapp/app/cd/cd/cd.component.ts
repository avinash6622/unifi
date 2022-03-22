import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Cd } from '../cd.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { CdService } from '../cd.service';

@Component({
    selector: 'jhi-cd',
    templateUrl: './cd.component.html'
})
export class CdComponent implements OnInit {
    account: Account;
    Commission: Cd[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'locationName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private _location: Location,
        private cdService: CdService
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.commissionlist(this.pageNumber);
    }

    commissionlist(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.cdService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe((res: HttpResponse<Cd[]>) => this.success(res.body, res.headers), (res: HttpResponse<any>) => this.error(res.body));
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.Commission = data;
    }

    error(data) {
        console.log(data);
    }

    deletecommission(commissions): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                console.log(commissions);
                this.cdService.delete(commissions.id).subscribe(data => {
                    this.Commission = this.Commission.filter(u => u !== commissions);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
                this.commissionlist(this.pageNumber);
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.commissionlist();
    }
}
