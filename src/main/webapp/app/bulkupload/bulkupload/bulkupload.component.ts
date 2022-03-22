import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BulkUpload } from '../bulkupload.model';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-bulkupload',
    templateUrl: './bulkupload.component.html',
    styleUrls: ['./bulkupload.component.css']
})
export class BulkUploadComponent implements OnInit {
    constructor(private router: Router, private _location: Location, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {}
}
