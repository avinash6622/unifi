import { Account } from './../../core/user/account.model';
import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ILocation } from '../location.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { LocationService } from './../location.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-location-add',
    templateUrl: './location-add.component.html'
})
export class LocationAddComponent implements OnInit {
    account: Account;
    isSaving: boolean;
    location: any;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();

    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
        private router: Router,
        private locationService: LocationService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            console.log(this.account);
        });
        this.location = {};
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    onSubmit() {
        this.locationService.add(this.location).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/location']);
                swal('Location added Successfully');
                this.activeModal.close();
            } else {
            }
        });
    }

    cancel() {
        this.activeModal.close();
    }
}
