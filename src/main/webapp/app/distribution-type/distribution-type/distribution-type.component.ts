import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DistributionTypeService } from './../distribution-type.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { DistributionTypeAddComponent } from '../distribution-type-add/distribution-type-add.component';
import { DistributionTypeUpdateComponent } from './../distribution-type-update/distribution-type-update.component';
import { HttpResponse } from '@angular/common/http';
import { DistType } from '../distribution-type.model';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-distribution-type',
    templateUrl: './distribution-type.component.html'
})
export class DistributionTypeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    distributiontypes: DistType[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'distTypeName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private distributiontypeService: DistributionTypeService,
        private parseLinks: JhiParseLinks,
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
        this.distributiontypeList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(DistributionTypeAddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.distributiontypeList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(DistributionTypeUpdateComponent);
        modalRef.componentInstance.distributiontypeID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.distributiontypeList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    distributiontypeList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.distributiontypeService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<DistType[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.distributiontypes = data;
        console.log(this.distributiontypes);
    }

    error(data) {
        console.log(data);
    }

    deleteDistributiontype(distributionType): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.distributiontypeService.delete(distributionType.id).subscribe(data => {
                    this.distributiontypes = this.distributiontypes.filter(u => u !== distributionType);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.distributiontypeList();
    }
}
