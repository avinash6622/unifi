import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IProduct } from '../product.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ProductService } from './../product.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-product-add',
    templateUrl: './product-add.component.html'
})
export class ProductAddComponent implements OnInit {
    isSaving: boolean;
    product: any;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.product = {};
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.productService.add(this.product).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/product']);
                swal('Product added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
