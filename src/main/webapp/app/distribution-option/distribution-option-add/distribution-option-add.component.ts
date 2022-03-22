import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Distribution } from '../distribution-option.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { DistributionService } from './../distribution-option.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { ProductService } from 'app/product/product.service';

@Component({
    selector: 'jhi-distribution-option-add',
    templateUrl: './distribution-option-add.component.html'
})
export class DistributionOptionAddComponent implements OnInit {
    isSaving: boolean;
    distribution: any;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    products: any[];

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private distributionService: DistributionService,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.distribution = {};
        if (this.distribution) {
            if (!this.distribution.product) {
                this.distribution['product'] = {
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
        this.distributionService.add(this.distribution).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/distributor-option']);
                swal('Distribution Option added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
