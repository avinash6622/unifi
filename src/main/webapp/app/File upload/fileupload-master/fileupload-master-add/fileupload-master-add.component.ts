import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ProductService } from 'app/product/product.service';
import { Fileupload } from '../../fileupload.model';
import { Subscription } from 'rxjs';
import { FileuploadService } from '../../fileupload.service';

@Component({
    selector: 'jhi-fileupload-master-add',
    templateUrl: './fileupload-master-add.component.html'
})
export class FileUploadMasterAddComponent implements OnInit {
    upload: Fileupload;
    links: any;
    inputFile: File;
    progress_status_percentage: number;
    formValid: boolean = false;
    products: any[];
    thingsSubscription: Subscription;
    detail: any;
    uploadStatus: boolean;
    uploadPmsStatus: boolean;

    constructor(
        private router: Router,
        private fileuploadservice: FileuploadService,
        private _location: Location,
        private productService: ProductService
    ) {}

    ngOnInit() {
        this.upload = {};
        this.progress_status_percentage = 5;
        this.products = [];
        this.productService.product().subscribe(products => {
            this.products = products;
            console.log(this.products, 'product');
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
        this.uploadStatus = false;
        if (this.formValid) {
            this.upload.startDate = new DatePipe('en-US').transform(this.upload.startDate, 'dd-MM-yyyy');
            this.upload.endDate = new DatePipe('en-US').transform(this.upload.endDate, 'dd-MM-yyyy');
            this.thingsSubscription = this.fileuploadservice.fileuploadMaster(this.upload, this.inputFile).subscribe(data => {
                this.detail = data;
                console.log(this.detail);
                if (this.detail && this.detail.id) {
                    this.router.navigate(['/masteruploads']);
                    swal('Uploads added Successfully');
                } else {
                    console.log('new');
                    if (this.detail.error === '470') {
                        this.uploadPmsStatus = true;
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
