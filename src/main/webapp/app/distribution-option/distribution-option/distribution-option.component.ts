import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DistributionService } from './../distribution-option.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { DistributionOptionAddComponent } from './../distribution-option-add/distribution-option-add.component';
import { DistributionOptionUpdateComponent } from './../distribution-option-update/distribution-option-update.component';
import { HttpResponse } from '@angular/common/http';
import { Distribution } from '../distribution-option.model';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-distribution-option',
    templateUrl: './distribution-option.component.html'
})
export class DistributionOptionComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    distributions: Distribution[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'optionName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private distributionService: DistributionService,
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
        this.distributionList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(DistributionOptionAddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.distributionList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(DistributionOptionUpdateComponent);
        modalRef.componentInstance.distributionID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.distributionList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    distributionList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.distributionService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Distribution[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.distributions = data;
    }

    error(data) {
        console.log(data);
    }

    deleteDistribution(distribution): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.distributionService.delete(distribution.id).subscribe(data => {
                    this.distributions = this.distributions.filter(u => u !== distribution);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.distributionList();
    }
}
