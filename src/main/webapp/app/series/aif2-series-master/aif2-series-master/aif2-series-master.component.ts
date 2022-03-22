import { AIF2Service } from './../aif2-series-master.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Aif2AddComponent } from './../aif2-series-master-add/aif2-series-master-add.component';
import { Aif2UpdateComponent } from './../aif2-series-master-update/aif2-series-master-update.component';
import { HttpResponse } from '@angular/common/http';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../../core/user/account.model';
import { OrderPipe } from 'ngx-order-pipe';
import { Aif2series } from '../aif2-series-master.model';

@Component({
    selector: 'jhi-aif2-series-master',
    templateUrl: './aif2-series-master.component.html'
})
export class AIF2Component implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    aifs: Aif2series[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortedCollection: any[];
    sortKey: any;
    order: string = 'classType';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private aifService: AIF2Service,
        private parseLinks: JhiParseLinks,
        private principal: Principal,
        private orderPipe: OrderPipe
    ) {
        this.sortKey = '';
        // this.sortedCollection = this.orderPipe.transform(this.products, 'product.productName');
        // console.log(this.sortedCollection);
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.aifList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(Aif2AddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.aifList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(Aif2UpdateComponent);
        modalRef.componentInstance.aifID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.aifList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    aifList(pageNumber?: number) {
        console.log(this.sortKey);
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.aifService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Aif2series[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.aifs = data;
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
