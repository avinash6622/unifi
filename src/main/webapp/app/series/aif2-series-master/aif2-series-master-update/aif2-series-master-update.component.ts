import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Aif2series } from '../aif2-series-master.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { AIF2Service } from '../aif2-series-master.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from 'app/product/product.service';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-aif2-series-master-update',
    templateUrl: './aif2-series-master-update.component.html'
})
export class Aif2UpdateComponent implements OnInit {
    isSaving: boolean;
    aif: Aif2series;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    products: any[];
    aifID: number;

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private aifervice: AIF2Service,
        private route: ActivatedRoute,
        private productService: ProductService
    ) {
        this.aif = {};
    }

    ngOnInit() {
        this.aifervice.find(this.aifID).subscribe(aif => (this.aif = aif));
        this.route.params.subscribe((params: any) => {
            const id = params.id;
            console.log(id);
        });
        if (this.aif) {
            if (!this.aif.product) {
                this.aif['product'] = {
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
        this.aifervice.update(this.aif).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/series']);
                swal('Aif2 Series Master Updated Successfully');
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
