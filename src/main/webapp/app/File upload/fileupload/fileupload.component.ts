import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FileuploadService } from './../fileupload.service';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Fileupload } from '../fileupload.model';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';

@Component({
    selector: 'jhi-fileupload',
    templateUrl: './fileupload.component.html',
    styleUrls: ['./fileupload.component.css']
})
export class FileuploadComponent implements OnInit {
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
        private alertService: JhiAlertService,
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
        this.fileUploadList(this.pageNumber);
    }

    fileUploadList(pageNumber?: number) {
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.fileuploads = [];
        this.fileuploadservice
            .list({
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

    fileApprove(fileupload) {
        this.fileuploadservice.pmsApprove(fileupload.id).subscribe(data => {
            this.approvedata = data;
            console.log(this.approvedata, 'approve');
            swal('Approved!', 'Your file has been Approved.', 'success');
            this.fileUploadList();
        });
    }

    deleteFileUpload(fileupload): void {
        console.log(fileupload);
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                console.log(result.value);
                this.fileuploadservice.delete(fileupload.id).subscribe(data => {
                    console.log(data);
                    this.fileuploads = this.fileuploads.filter(u => u !== fileupload);
                    console.log(this.fileuploads);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }
}
