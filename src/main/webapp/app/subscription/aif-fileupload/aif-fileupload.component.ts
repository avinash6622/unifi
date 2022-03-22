import { AifUpload } from './aifupload.model';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Location, DatePipe } from '@angular/common';
import { AifUploadService } from './aifupload.service';
import { HttpRequest } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-aif-fileupload',
    templateUrl: './aif-fileupload.component.html'
})
export class AifUploadComponent implements OnInit {
    upload: AifUpload;
    links: any;
    inputFile: File;
    progress_status_percentage: number;
    formValid: boolean = false;
    thingsSubscription: Subscription;
    errorStatus: boolean;
    message: any;

    constructor(private router: Router, private fileuploadservice: AifUploadService, private _location: Location) {}

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

    onDateChange(event) {
        console.log(event.target.value);
        if (event.target.value.match(/\d{2}-\d{3}-\d{4}/)) {
            this.upload.redemptionDate = event.target.value;
            console.log(this.upload.redemptionDate, '123');
        }
    }

    cancel() {
        this._location.back();
    }

    onSubmit() {
        this.errorStatus = false;
        this.upload.redemptionDate = new DatePipe('en-US').transform(this.upload.redemptionDate, 'dd-MMM-yyyy');
        this.thingsSubscription = this.fileuploadservice.aifUploadFile(this.upload, this.inputFile).subscribe(data => {
            this.message = data;
            if (data === null) {
                swal('Upload added Successfully');
            }
            if (this.message.error) {
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
