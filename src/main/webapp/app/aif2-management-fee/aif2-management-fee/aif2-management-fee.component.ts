import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AIF2FeeService } from './../aif2-management-fee.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { AIF2Fee } from '../aif2-management-fee.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead/typeahead-match.class';

@Component({
    selector: 'jhi-aif2-management-fee',
    templateUrl: './aif2-management-fee.component.html'
})
export class AIF2FeeComponent implements OnInit {
    account: Account;
    aifs: AIF2Fee[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'rmName';
    reverse: boolean = false;
    PaginationFlag: boolean = true;
    constructor(
        private router: Router,
        private aifService: AIF2FeeService,
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
        this.aifList(this.pageNumber);
    }

    backClicked() {
        this._location.back();
    }

    aifList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.PaginationFlag = true;
        this.aifs = [];
        this.aifService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<AIF2Fee[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.aifs = data;
        console.log(this.aifs);
    }

    error(data) {
        console.log(data);
    }

    deleteAif(aif): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.aifService.delete(aif.id).subscribe(data => {
                    this.aifs = this.aifs.filter(u => u !== aif);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.aifList();
    }
}
