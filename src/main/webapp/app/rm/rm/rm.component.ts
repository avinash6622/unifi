import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RmService } from './../rm.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Rm } from '../rm.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead/typeahead-match.class';

@Component({
    selector: 'jhi-rm',
    templateUrl: './rm.component.html'
})
export class RmComponent implements OnInit {
    account: Account;
    relationMasters: Rm[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'rmName';
    reverse: boolean = false;
    rmFilterPrediction: any = [];
    relationshipManager: any = [];
    PaginationFlag: boolean = true;
    constructor(
        private router: Router,
        private rmService: RmService,
        private parseLinks: JhiParseLinks,
        private _location: Location,
        private principal: Principal
    ) {
        this.sortKey = '';
        this.rmFilterPrediction = {};
        this.relationshipManager = {};
        this.filterByrmName();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.rmList(this.pageNumber);
    }

    filterByrmName() {
        this.rmService.get().subscribe(_data => {
            this.rmFilterPrediction = _data;
        });
    }

    GetfilterByrmName() {
        if (!this.relationshipManager.rmName) {
            this.rmList(this.pageNumber);
        } else {
            this.rmService.searchRm(this.relationshipManager).subscribe(data => {
                console.log('data', data);
                this.PaginationFlag = false;
                this.relationMasters = data;
            });
        }
    }

    backClicked() {
        this._location.back();
    }

    rmList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.PaginationFlag = true;
        this.relationMasters = [];
        this.rmService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe((res: HttpResponse<Rm[]>) => this.success(res.body, res.headers), (res: HttpResponse<any>) => this.error(res.body));
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.relationMasters = data;
        console.log(this.relationMasters);
    }

    error(data) {
        console.log(data);
    }

    deleteRm(relationMaster): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.rmService.delete(relationMaster.id).subscribe(data => {
                    this.relationMasters = this.relationMasters.filter(u => u !== relationMaster);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    onSelect(event: TypeaheadMatch): void {
        this.rmFilterPrediction.rmName = event.item;
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.rmList();
    }
}
