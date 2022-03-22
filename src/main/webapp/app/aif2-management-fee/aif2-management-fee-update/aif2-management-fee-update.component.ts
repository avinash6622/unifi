import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AIF2FeeService } from './../aif2-management-fee.service';
import { UserService } from 'app/core';
import { AIF2Service } from 'app/series/aif2-series-master/aif2-series-master.service';
import { ProductService } from 'app/product/product.service';
import { AIF2Fee } from '../aif2-management-fee.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-aif2-management-fee-update',
    templateUrl: './aif2-management-fee-update.component.html'
})
export class Aif2UpdateComponent implements OnInit {
    aif: AIF2Fee;
    seriesMasters: any;
    id: number;
    products: any[];
    dropdownSettings: any;

    constructor(
        private router: Router,
        private userService: UserService,
        private aifService: AIF2FeeService,
        private seriesService: AIF2Service,
        private _location: Location,
        private route: ActivatedRoute,
        private productService: ProductService
    ) {
        this.aif = {};
        this.dropdownSettings = {
            singleSelection: true,
            idField: 'id',
            textField: 'productName',
            itemsShowLimit: 10,
            allowSearchFilter: true
        };
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadRm(this.id);
        });
        if (this.aif) {
            if (!this.aif.aif2SeriesMaster) {
                this.aif['aif2SeriesMaster'] = {
                    id: null
                };
            }
            if (!this.aif.product) {
                this.aif['product'] = {
                    id: null
                };
            }
        }
        this.seriesService.aif2Series().subscribe(seriesMasters => {
            this.seriesMasters = seriesMasters;
        });
    }

    onItemSelect(item: any) {
        this.aif.product = this.products.filter(itm => {
            return itm.id === item.id;
        })[0];
        this.seriesService.aif2SeriesandBlend(this.aif.product.id).subscribe(seriesMasters => {
            this.seriesMasters = seriesMasters;
        });
    }

    onSelectAll(items: any) {
        console.log(items);
    }

    cancel() {
        this._location.back();
    }

    loadRm(id) {
        this.aifService.find(id).subscribe(aif => {
            this.aif = aif;
            if (this.aif) {
                if (!this.aif.aif2SeriesMaster) {
                    this.aif['aif2SeriesMaster'] = {
                        id: null
                    };
                }
            }
            this.products = [];
            this.productService.aif2product().subscribe(products => {
                this.products = products;
                console.log(this.products, 'product');
            });
        });
    }

    onSubmit() {
        if (this.aif) {
            if (this.aif) {
                if (!this.aif.aif2SeriesMaster.id) {
                    this.aif['aif2SeriesMaster'] = null;
                }
            }
        }
        this.aifService.update(this.aif).subscribe(data => {
            if (data) {
                this.router.navigate(['/aif2-management-fee']);
                swal('AIF2 Management Fee Updated Successfully');
            } else {
            }
        });
    }
}
