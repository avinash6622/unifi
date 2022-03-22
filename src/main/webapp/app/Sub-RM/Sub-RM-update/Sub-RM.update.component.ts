import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SubRM } from '../Sub-RM.model';
import { SubRMService } from './../Sub-RM.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-subrm-update',
    templateUrl: './Sub-RM-update.component.html'
})
export class SubRMUpdateComponent implements OnInit {
    isSaving: boolean;
    subRMs: SubRM;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    subRMID: number;

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private subRMService: SubRMService,
        private route: ActivatedRoute
    ) {
        this.subRMs = {};
    }

    ngOnInit() {
        this.subRMService.find(this.subRMID).subscribe(subRMs => (this.subRMs = subRMs));
        this.route.params.subscribe((params: any) => {
            const id = params.id;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.subRMService.update(this.subRMs).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/sub-rm']);
                swal(' SubRM Updated Successfully');
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
