import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DistributionTypeService } from './../distribution-type.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { DistType } from 'app/distribution-type/distribution-type.model';
import { ProductService } from 'app/product/product.service';

@Component({
    selector: 'jhi-distribution-type-update',
    templateUrl: './distribution-type-update.component.html'
})
export class DistributionTypeUpdateComponent implements OnInit {
    isSaving: boolean;
    distributiontype: DistType;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    distributiontypeID: number;
    products: any[];

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private distributiontypeService: DistributionTypeService,
        private route: ActivatedRoute,
        private productService: ProductService
    ) {
        this.distributiontype = {};
    }

    ngOnInit() {
        console.log(this.distributiontypeID, 'nill');
        this.distributiontypeService
            .find(this.distributiontypeID)
            .subscribe(distributiontype => (this.distributiontype = distributiontype));
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
        this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.distributiontypeService.update(this.distributiontype).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/distributor-type']);
                swal('Distribution Type Updated Successfully');
                this.activeModal.close();
            } else {
                console.log('error');
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
