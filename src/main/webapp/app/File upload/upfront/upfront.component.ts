import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FileuploadService } from './../fileupload.service';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Fileupload } from '../fileupload.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-upfront',
    templateUrl: './upfront.component.html',
    styleUrls: ['./upfront.css']
})
export class UpfrontComponent implements OnInit {
    account: Account;
    fileuploads: Fileupload[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    approvedata: any;

    constructor(
        private router: Router,
        private fileuploadservice: FileuploadService,
        private _location: Location,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            console.log(this.account);
        });
        this.listPage = 0;
        this.listItesmsPerPage = 10;
        this.UpfrontList(this.pageNumber);
    }

    backClicked() {
        this._location.back();
    }

    UpfrontList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.fileuploads = [];
        this.fileuploadservice
            .upfrontList({
                page: this.listPage - 1,
                size: this.listItesmsPerPage
            })
            .subscribe(
                (res: HttpResponse<Fileupload[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.fileuploads = data;
    }

    error(data) {
        console.log(data);
    }

    deleteFileupolad(fileupload): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.fileuploadservice.deleteUpfront(fileupload.id).subscribe(data => {
                    this.fileuploads = this.fileuploads.filter(u => u !== fileupload);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    fileApprove(fileupload) {
        this.fileuploadservice.upfrontApprove(fileupload.id).subscribe(data => {
            this.approvedata = data;
            console.log(this.approvedata, 'approve');
            swal('Approved!', 'Your file has been Approved.', 'success');
            this.UpfrontList();
        });
    }

    // openFileupolad(fileupload): void {
    //     swal({
    //         showCancelButton: true,
    //         confirmButtonColor: '#4CAF50',
    //         cancelButtonColor: '#4CAF50',
    //     });
    //     // .then(result => {
    //     //     if (result.value) {
    //     //         this.fileuploadservice.delete(fileupload.id).subscribe(data => {
    //     //             this.fileuploads = this.fileuploads.filter(u => u !== fileupload);
    //     //         });
    //             swal('Success!', 'Your file has been Added.', 'success');
    //         }
    //     // });
}
