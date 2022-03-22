import { FileuploadService } from './../../fileupload.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { ProductService } from 'app/product/product.service';

@Component({
    selector: 'jhi-aifupload-add',
    templateUrl: './aifupload-add.component.html'
})
export class AifUploadAddComponent implements OnInit {
    upload: any = [];
    aifBlend: any = [];
    links: any;
    inputFile: File;
    progress_status_percentage: number;
    formValid: boolean = false;
    thingsSubscription: Subscription;
    detail: any;
    uploadAifStatus: boolean;
    uploadStatus: boolean;
    aifProductDat: any;
    constructor(
        private router: Router,
        private fileuploadservice: FileuploadService,
        private _location: Location,
        private _ProductService: ProductService
    ) {}

    ngOnInit() {
        this.aifProductDat = [];
        this.upload = {};
        this.progress_status_percentage = 5;
        this._ProductService.aif2product().subscribe(data => {
            this.aifProductDat = data;
        });
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
        this.formValid = true;
        console.log('this.upload.startDate', this.upload.startDate);
        console.log('this.upload.endDate', this.upload.endDate);
        this.aifBlend.startDate = new DatePipe('en-US').transform(this.upload.startDate, 'dd-MM-yyyy');
        this.aifBlend.endDate = new DatePipe('en-US').transform(this.upload.endDate, 'dd-MM-yyyy');
        this.upload.startDate = new DatePipe('en-US').transform(this.upload.startDate, 'dd-MM-yyyy');
        this.upload.endDate = new DatePipe('en-US').transform(this.upload.endDate, 'dd-MM-yyyy');
        console.log('this.upload.fileName', this.upload.fileName);
        // http://localhost:9000/swagger-ui/index.html#!/file45upload45resource/uploadUmbrellaManagementFeeUsingPOST
        // POST /api/upload/umbrella/managementFee
        if (this.upload.fileName == 'Aif Blend 2') {
            console.log(this.upload);
            //this.upload.fileName =
            this.thingsSubscription = this.fileuploadservice.aifTransactionUploadFile(this.upload, this.inputFile).subscribe(data => {
                this.detail = data;
                console.log('this.detail', this.detail);
                console.log(this.detail);
                if (this.detail.error === '409') {
                    swal(this.detail.message);
                }
                if (this.detail.error === '412') {
                    swal(this.detail.message);
                }
                if (this.detail && this.detail.id) {
                    this.router.navigate(['/aifuploads']);
                    swal('Uploads added Successfully');
                } else {
                    if (this.detail.error === '470') {
                        this.uploadAifStatus = true;
                        swal(this.detail.message);
                    } else if (this.detail.error === '450') {
                        this.uploadStatus = true;
                        swal(this.detail.message);
                    } else {
                        swal(this.detail.message);
                    }
                }
            });
        } else if (this.upload.fileName == 'AIF Umbrella Management Fee') {
            console.log('this.aifProductDat', this.aifProductDat);
            Object.keys(this.aifProductDat).map(data => {
                console.log("data['productName']", this.aifProductDat[data]['productName']);
                if (this.aifProductDat[data]['productName'] == 'UNIFI AIF Umbrella Blend Fund - 2') {
                    console.log('23234234234');
                    this.aifBlend.productId = this.aifProductDat[data]['id'];
                    console.log('this.aifBlend', this.aifBlend);
                }
            });
            console.log(this.upload);
            //this.upload.fileName =

            this.thingsSubscription = this.fileuploadservice
                .aifBlendTransactionUploadFile(this.aifBlend, this.inputFile)
                .subscribe(data => {
                    this.detail = data;
                    console.log('this.detail', this.detail);
                    console.log(this.detail);
                    if (this.detail.error === '409') {
                        swal(this.detail.message);
                    }
                    if (this.detail.error === '412') {
                        swal(this.detail.message);
                    }
                    if (this.detail && this.detail.id) {
                        this.router.navigate(['/aifuploads']);
                        swal('Uploads added Successfully');
                    } else {
                        if (this.detail.error === '470') {
                            this.uploadAifStatus = true;
                            swal(this.detail.message);
                        } else if (this.detail.error === '450') {
                            this.uploadStatus = true;
                            swal(this.detail.message);
                        } else {
                            swal(this.detail.message);
                        }
                    }
                });
        } else {
            this.thingsSubscription = this.fileuploadservice.aifUploadFile(this.upload, this.inputFile).subscribe(data => {
                this.detail = data;
                console.log(this.detail);
                if (this.detail.error === '409') {
                    swal(this.detail.message);
                }
                if (this.detail.error === '412') {
                    swal(this.detail.message);
                }
                if (this.detail && this.detail.id) {
                    this.router.navigate(['/aifuploads']);
                    swal('Uploads added Successfully');
                } else {
                    if (this.detail.error === '470') {
                        this.uploadAifStatus = true;
                        swal(this.detail.message);
                    } else if (this.detail.error === '450') {
                        this.uploadStatus = true;
                        swal(this.detail.message);
                    } else {
                        swal(this.detail.message);
                    }
                }
            });
        }
    }
}
