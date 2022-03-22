import { Fileupload } from './../fileupload.model';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { FileuploadService } from './../fileupload.service';
import { DatePipe } from '@angular/common';
import { formArrayNameProvider } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { HttpRequest } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-upload-add',
    templateUrl: './upload-add.component.html',
    styleUrls: ['./upload-add.component.css']
})
export class UploadAddComponent implements OnInit {
    upload: any = [];
    links: any;
    inputFile: File;
    progress_status_percentage: number;
    formValid: boolean = false;
    thingsSubscription: Subscription;
    detail: any;
    uploadPmsStatus: boolean;

    constructor(private router: Router, private fileuploadservice: FileuploadService, private _location: Location) {}

    ngOnInit() {
        this.upload = {};
        this.progress_status_percentage = 5;
    }

    onDateChange(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.upload.startDate = event.target.value;
            console.log(this.upload.startDate, '123');
        }
    }

    onDateChange1(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{2}-\d{4}/)) {
            this.upload.endDate = event.target.value;
            console.log(this.upload.endDate, '123');
        }
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
        this.uploadPmsStatus = false;
        this.upload.startDate = new DatePipe('en-US').transform(this.upload.startDate, 'dd-MM-yyyy');
        this.upload.endDate = new DatePipe('en-US').transform(this.upload.endDate, 'dd-MM-yyyy');

        this.thingsSubscription = this.fileuploadservice.sendUploadFile(this.upload, this.inputFile).subscribe(data => {
            console.log('data', data);
            this.detail = data;
            console.log(this.detail);
            if (this.detail && this.detail.id) {
                this.router.navigate(['/uploads']);
                swal('Uploads added Successfully');
            } else {
                console.log('new');
                if (this.detail.error === '470') {
                    this.uploadPmsStatus = true;
                    swal(this.detail.message);
                } else if (this.detail.error === '409') {
                    swal(this.detail.message);
                } else if (this.detail.error === '400') {
                    swal(this.detail.message);
                } else {
                    swal(this.detail.message);
                }
            }
        });

        // }
    }
}
