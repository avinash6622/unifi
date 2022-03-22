import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SubRMService } from './../Sub-RM.service';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { SubRMAddComponent } from './../Sub-RM-add/Sub-RM-add.component';
import { HttpResponse } from '@angular/common/http';
import swal from 'sweetalert2';
import { SubRM } from '../Sub-RM.model';
import { SubRMUpdateComponent } from '../Sub-RM-update/Sub-RM.update.component';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead/typeahead-match.class';

@Component({
    selector: 'jhi-subrm',
    templateUrl: './Sub-RM.component.html'
})
export class SubRMComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    subrms: SubRM[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'subName';
    reverse: boolean = false;
    subRm: any = [];
    findScreenData: any = [];
    PaginationFlag: boolean = true;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private parseLinks: JhiParseLinks,
        private subRMService: SubRMService,
        private principal: Principal
    ) {
        this.sortKey = '';
        this.findScreenData = {};
        this.subRm = {};
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.SubRMList(this.pageNumber);
        this.getSubRmDetails();
    }

    onSelect(event: TypeaheadMatch): void {
        this.subRm.subName = event.item;
    }

    SubRMList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.PaginationFlag = true;
        this.subRMService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<SubRM[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    getSubRmDetails() {
        this.subRMService.get().subscribe(data => {
            this.findScreenData = data;
            console.log('findScreenData', this.findScreenData);
        });
    }

    getSubRmDetailsBySearch() {
        if (this.subRm.subName) {
            this.subRMService.findBySubName(this.subRm).subscribe(
                res => {
                    this._success(res);
                    console.log('res.body', res);
                    this.PaginationFlag = false;
                },
                (res: HttpResponse<any>) => this.error(res.body)
            );
        } else {
            this.SubRMList(this.pageNumber);
        }
    }

    success(data, headers) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.subrms = data;
        console.log(this.subrms, 'subrm');
    }

    _success(data) {
        console.log('data', data);
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.listTotalItems = headers.get('x-total-count');
        this.subrms = data;
        console.log(this.subrms, 'subrms');
    }

    error(data) {
        console.log(data);
    }

    openAddDialog(id?: number) {
        const modalRef = this.dialog.open(SubRMAddComponent);
        modalRef.componentInstance.productID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.SubRMList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    deletesubrm(subRM): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.subRMService.delete(subRM.id).subscribe(data => {
                    this.subrms = this.subrms.filter(u => u !== subRM);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }
    openUpdateDialog(id?: number) {
        const modalRef = this.dialog.open(SubRMUpdateComponent);
        modalRef.componentInstance.subRMID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.SubRMList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.SubRMList();
    }
}
