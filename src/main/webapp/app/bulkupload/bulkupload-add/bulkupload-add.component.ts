import { BulkUpload } from '../bulkupload.model';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { FileuploadService } from '../bulkupload.service';
import { HttpRequest } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-bulkupload-add',
    templateUrl: './bulkupload-add.component.html',
    styleUrls: ['./bulkupload-add.component.css']
})
export class BulkUploadAddComponent implements OnInit {
    upload: BulkUpload;
    links: any;
    inputFile: File;
    progress_status_percentage: number;
    formValid: boolean = false;
    thingsSubscription: Subscription;
    errorStatus: boolean;
    message: any;

    constructor(private router: Router, private fileuploadservice: FileuploadService, private _location: Location) {}

    ngOnInit() {
        this.upload = {};
    }

    fileChange(event) {
        const fileList: FileList = event.target.files;
        console.log(fileList);
        if (fileList.length > 0) {
            const file: File = fileList[0];
            console.log('file', file);
            this.inputFile = file;
        }
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        this.errorStatus = false;
        if (this.upload.distName === 'Client Master') {
            console.log(this.upload.distName);
            this.thingsSubscription = this.fileuploadservice.sendUploadFile(this.upload, this.inputFile).subscribe(data => {
                this.message = data;
                console.log(data, 'data');
                if (this.message.error === '450') {
                    console.log('error');
                    swal(this.message.message);
                    this.errorStatus = true;
                } else {
                    console.log('success');
                    swal('Upload added Successfully');
                }
            });
        }
        if (this.upload.distName === 'Distributor Master') {
            console.log(this.upload.distName);
            console.log('distributor');
            this.thingsSubscription = this.fileuploadservice.distupload(this.upload, this.inputFile).subscribe(data => {
                this.message = data;
                if (data === null) {
                    swal('Upload added Successfully');
                }
                if (this.message.error === '450') {
                    console.log('error');
                    swal(this.message.message);
                    this.errorStatus = true;
                } else {
                    console.log('success');
                    swal('Upload added Successfully');
                }
            });
        }
    }
}
