import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Aif2series } from '../aif2-series-master.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { AIF2Service } from '../aif2-series-master.service';
import { ProductService } from 'app/product/product.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-aif2-series-master-add',
    templateUrl: './aif2-series-master-add.component.html'
})
export class Aif2AddComponent implements OnInit {
    isSaving: boolean;
    aif: Aif2series;
    formSubmitted: boolean;
    products: any[];
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private aifService: AIF2Service,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.aif = {};
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
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.aifService.add(this.aif).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/series']);
                swal('aif added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
