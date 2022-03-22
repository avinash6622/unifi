import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SubRM } from '../Sub-RM.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { SubRMService } from './../Sub-RM.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-Sub-RM-add',
    templateUrl: './Sub-RM-add.component.html'
})
export class SubRMAddComponent implements OnInit {
    isSaving: boolean;
    subRM: any;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private SubRMService: SubRMService
    ) {}

    ngOnInit() {
        this.subRM = {};
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.SubRMService.add(this.subRM).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/sub-rm']);
                swal('SubRM added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
