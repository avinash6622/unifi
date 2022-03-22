import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Investment } from '../investment.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { InvestmentService } from './../investment.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-investment-add',
    templateUrl: './investment-add.component.html'
})
export class InvestmentAddComponent implements OnInit {
    isSaving: boolean;
    investment: any;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private investmentService: InvestmentService
    ) {}

    ngOnInit() {
        this.investment = {};
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.investmentService.add(this.investment).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/investment']);
                swal('Investment added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
