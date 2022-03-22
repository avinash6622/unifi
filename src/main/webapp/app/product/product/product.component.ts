import { ProductService } from './../product.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ProductAddComponent } from './../product-add/product-add.component';
import { ProductUpdateComponent } from './../product-update/product-update.component';
import { HttpResponse } from '@angular/common/http';
import { IProduct } from '../product.model';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { OrderPipe } from 'ngx-order-pipe';

@Component({
    selector: 'jhi-product',
    templateUrl: './product.component.html'
})
export class ProductComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    products: IProduct[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortedCollection: any[];
    sortKey: any;
    order: string = 'productName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private productService: ProductService,
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
        this.productList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(ProductAddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.productList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(ProductUpdateComponent);
        modalRef.componentInstance.productID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.productList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    productList(pageNumber?: number) {
        console.log(this.sortKey);
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.productService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<IProduct[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        console.log(data);
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.products = data;
    }

    error(data) {
        console.log(data);
    }

    deleteProduct(product): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.productService.delete(product.id).subscribe(data => {
                    this.products = this.products.filter(u => u !== product);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        if (this.order === value) {
            this.reverse = !this.reverse;
        }

        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.productList();
    }
}
