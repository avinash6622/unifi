import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Investment } from '../investment.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { InvestmentService } from './../investment.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-investment-update',
    templateUrl: './investment-update.component.html'
})
export class InvestmentUpdateComponent implements OnInit {
    isSaving: boolean;
    investments: Investment;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    investmentID: number;

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private investmentService: InvestmentService,
        private route: ActivatedRoute
    ) {
        this.investments = {};
    }

    ngOnInit() {
        this.investmentService.find(this.investmentID).subscribe(investments => (this.investments = investments));
        this.route.params.subscribe((params: any) => {
            const id = params.id;
            console.log(id);
        });
        // const id = +this.route.snapshot.paramMap.get('id');
        // console.log(id);
        // this.locationService.find(id)
        // .subscribe(location => this.locations = location);
        // console.log(this.locations);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.investmentService.update(this.investments).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/investment']);
                swal('Investment Updated Successfully');
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
