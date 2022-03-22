import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ILocation } from '../location.model';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { LocationService } from './../location.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-location-update',
    templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
    isSaving: boolean;
    locations: ILocation;
    formSubmitted: boolean;
    onFormSubmit = new EventEmitter();
    locationID: number;

    constructor(
        public activeModal: NgbActiveModal,
        private router: Router,
        private locationService: LocationService,
        private route: ActivatedRoute
    ) {
        this.locations = {};
    }

    ngOnInit() {
        this.locationService.find(this.locationID).subscribe(locations => (this.locations = locations));
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
        this.locationService.update(this.locations).subscribe(data => {
            if (data) {
                this.formSubmitted = true;
                this.onFormSubmit.emit('submitted');
                this.router.navigate(['/location']);
                swal('Location Updated Successfully');
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
