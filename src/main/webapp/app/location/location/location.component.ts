import { LocationService } from './../location.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { LocationAddComponent } from './../location-add/location-add.component';
import { LocationUpdateComponent } from './../location-update/location-update.component';
import { HttpResponse } from '@angular/common/http';
import { ILocation } from '../location.model';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-location',
    templateUrl: './location.component.html'
})
export class LocationComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    locations: ILocation[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'locationName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private locationService: LocationService,
        private parseLinks: JhiParseLinks,
        private principal: Principal
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.locationList(this.pageNumber);
    }

    openAddDialog() {
        const modalRef = this.dialog.open(LocationAddComponent);
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.locationList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    openUpdateialog(id?: number) {
        const modalRef = this.dialog.open(LocationUpdateComponent);
        modalRef.componentInstance.locationID = id;
        modalRef.componentInstance.onFormSubmit.subscribe(action => {
            if (action === 'submitted') {
                console.log('submitted');
                this.locationList(this.pageNumber);
            } else {
                console.log('cancelled');
            }
        });
    }

    locationList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.locationService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Location[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.locations = data;
    }

    error(data) {
        console.log(data);
    }

    deleteLocation(location): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.locationService.delete(location.id).subscribe(data => {
                    this.locations = this.locations.filter(u => u !== location);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.locationList();
    }
}
