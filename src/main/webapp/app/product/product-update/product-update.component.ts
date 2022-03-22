import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IProduct } from '../product.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ProductService } from './../product.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-product-update',
    templateUrl: './product-update.component.html'
})
export class ProductUpdateComponent implements OnInit {
    isSaving: boolean;
    products: IProduct;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    productID: number;

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private productService: ProductService,
        private route: ActivatedRoute
    ) {
        this.products = {};
    }

    ngOnInit() {
        this.productService.find(this.productID).subscribe(products => (this.products = products));
        this.route.params.subscribe((params: any) => {
            const id = params.id;
            console.log(id);
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.productService.update(this.products).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/product']);
                swal('Product Updated Successfully');
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
