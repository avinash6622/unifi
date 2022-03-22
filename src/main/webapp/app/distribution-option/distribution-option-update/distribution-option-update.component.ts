import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Distribution } from '../distribution-option.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { DistributionService } from './../distribution-option.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { ProductService } from 'app/product/product.service';

@Component({
    selector: 'jhi-distribution-option-update',
    templateUrl: './distribution-option-update.component.html'
})
export class DistributionOptionUpdateComponent implements OnInit {
    isSaving: boolean;
    distributions: Distribution;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    distributionID: number;
    products: any[];

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private distributionService: DistributionService,
        private productService: ProductService,
        private route: ActivatedRoute
    ) {
        this.distributions = {};
    }

    ngOnInit() {
        this.distributionService.find(this.distributionID).subscribe(distributions => (this.distributions = distributions));
        this.route.params.subscribe((params: any) => {
            const id = params.id;
            console.log(id);
            if (this.distributions) {
                if (!this.distributions.product) {
                    this.distributions['product'] = {
                        id: null
                    };
                }
            }
        });
        if (this.distributions) {
            if (!this.distributions.product) {
                this.distributions['product'] = {
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
        this.distributionService.update(this.distributions).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/distributor-option']);
                swal('Distribution Option Updated Successfully');
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
