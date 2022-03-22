import { InvestmentService } from './../investment.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { InvestmentAddComponent } from './../investment-add/investment-add.component';
import { InvestmentUpdateComponent } from './../investment-update/investment-update.component';
import { HttpResponse } from '@angular/common/http';
import { Investment } from '../investment.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-investment',
    templateUrl: './investment.component.html'
})
export class InvestmentComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    investments: Investment[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'investmentName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private investmentService: InvestmentService,
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
        this.investmentList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(InvestmentAddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.investmentList();
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(InvestmentUpdateComponent);
        modalRef.componentInstance.investmentID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.investmentList();
            } else {
                console.log('cancelled');
            }
        });
    }

    investmentList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.investmentService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Investment[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.investments = data;
        console.log(this.investments);
    }

    error(data) {
        console.log(data);
    }

    deleteInvestment(investment): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.investmentService.delete(investment.id).subscribe(data => {
                    this.investments = this.investments.filter(u => u !== investment);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.investmentList();
    }
}
