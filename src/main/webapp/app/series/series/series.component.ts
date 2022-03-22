import { SeriesService } from './../series.service';
import { Series } from './../series.model';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import swal from 'sweetalert2';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-series',
    templateUrl: './series.component.html',
    styleUrls: ['./series.css']
})
export class SeriesComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    series: Series[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'seriesName';
    reverse: boolean = false;

    constructor(
        private router: Router,
        private seriesService: SeriesService,
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
        this.listItesmsPerPage = 25;
        this.seriesList(this.pageNumber);
    }

    seriesList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.seriesService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Series[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.series = data;
    }

    error(data) {
        console.log(data);
    }

    deleteSeries(serie): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.seriesService.delete(serie.id).subscribe(data => {
                    this.series = this.series.filter(u => u !== serie);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.seriesList();
    }
}
