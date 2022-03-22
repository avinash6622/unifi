import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { DistributionTypeService } from './../distribution-type.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { ProductService } from 'app/product/product.service';
import { DistType } from 'app/distribution-type/distribution-type.model';

@Component({
    selector: 'jhi-distribution-type-add',
    templateUrl: './distribution-type-add.component.html'
})
export class DistributionTypeAddComponent implements OnInit {
    isSaving: boolean;
    distributiontype: DistType;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    products: any[];

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private distributiontypeService: DistributionTypeService,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.distributiontype = {};
        if (this.distributiontype) {
            if (!this.distributiontype.product) {
                this.distributiontype['product'] = {
                    id: null
                };
            }
        }
        this.products = [];
        this.productService.product().subscribe(products => {
            this.products = products;
            console.log(this.products, 'product');
        });
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.distributiontypeService.add(this.distributiontype).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/distributor-type']);
                swal('Distribution Type added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
