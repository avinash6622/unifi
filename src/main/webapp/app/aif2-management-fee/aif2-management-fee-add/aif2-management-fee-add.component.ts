import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AIF2FeeService } from './../aif2-management-fee.service';
import { UserService } from 'app/core';
import { AIF2Service } from 'app/series/aif2-series-master/aif2-series-master.service';
import { ProductService } from 'app/product/product.service';
import { AIF2Fee } from '../aif2-management-fee.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'jhi-aif2-management-fee-add',
    templateUrl: './aif2-management-fee-add.component.html'
})
export class AIF2AddComponent implements OnInit {
    aif: AIF2Fee;
    subaifs: any[];
    seriesMasters: any;
    products: any[];
    dropdownSettings: any;

    constructor(
        private router: Router,
        private userService: UserService,
        private aifService: AIF2FeeService,
        private seriesService: AIF2Service,
        private _location: Location,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.aif = {};
        if (this.aif) {
            if (!this.aif.aif2SeriesMaster) {
                this.aif.aif2SeriesMaster = null;
            }
            if (!this.aif.product) {
                this.aif['product'] = {
                    id: null
                };
            }
        }
        // this.seriesService.aif2Series().subscribe(seriesMasters => {
        //     this.seriesMasters = seriesMasters;
        // });
        this.products = [];
        this.productService.aif2product().subscribe(products => {
            this.products = products;
            console.log(this.products, 'product');
        });
        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'productName',
            itemsShowLimit: 10,
            allowSearchFilter: true
        };
    }

    onItemSelect(item: any) {
        console.log(item);
        if (this.products) {
            this.aif.product = this.products.filter(itm => {
                return itm.id === item.id;
            })[0];
            this.seriesService.aif2SeriesandBlend(item.id).subscribe(seriesMasters => {
                this.seriesMasters = seriesMasters;
            });
        }
        console.log(this.aif.product);
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        console.log(this.aif.product.productName);
        if (this.aif.product.productName === 'AIF2') {
            // this.aif.monthYear = new DatePipe('en-US').transform(this.aif.monthYear, 'dd-MM-yyyy');
            console.log(this.aif.monthYear, 'year');
            if (this.aif) {
                if (this.aif.aif2SeriesMaster === null) {
                    this.aif.aif2SeriesMaster = null;
                }
            }
            this.aifService.add(this.aif).subscribe(data => {
                if (data) {
                    this.router.navigate(['/aif2-management-fee']);
                    swal('Aif2 Management Fee added Successfully');
                } else {
                }
            });
        }
        if (this.aif.product.productName === 'AIF Blend') {
            // this.aif.monthYear = new DatePipe('en-US').transform(this.aif.monthYear, 'dd-MM-yyyy');
            console.log(this.aif.monthYear, 'year');
            if (this.aif) {
                if (this.aif.aif2SeriesMaster === null) {
                    this.aif.aif2SeriesMaster = null;
                }
            }
            this.aifService.add(this.aif).subscribe(data => {
                if (data) {
                    this.router.navigate(['/aif2-management-fee']);
                    swal('Aif Blend Management Fee added Successfully');
                } else {
                }
            });
        }
        if (this.aif.product.productName === 'UNIFI AIF Umbrella Blend Fund - 2') {
            // this.aif.monthYear = new DatePipe('en-US').transform(this.aif.monthYear, 'dd-MM-yyyy');
            console.log(this.aif.monthYear, 'year');
            if (this.aif) {
                if (this.aif.aif2SeriesMaster === null) {
                    this.aif.aif2SeriesMaster = null;
                }
            }
            this.aifService.add(this.aif).subscribe(data => {
                if (data) {
                    this.router.navigate(['/aif2-management-fee']);
                    swal('AIF Umbrella Blend Fund - 2 added Successfully');
                } else {
                }
            });
        }
    }
}
