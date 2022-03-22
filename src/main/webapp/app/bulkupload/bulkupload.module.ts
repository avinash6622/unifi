import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { fileuploadState } from './bulkupload.route';
import { BulkUploadComponent } from './bulkupload/bulkupload.component';
import { BulkUploadAddComponent } from './bulkupload-add/bulkupload-add.component';
import { FileuploadService } from './bulkupload.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { BusyModule } from 'ngx-busy';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

@NgModule({
    imports: [
        BusyModule,
        NgxPaginationModule,
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(fileuploadState),
        FormsModule,
        CommonModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [BulkUploadComponent, BulkUploadAddComponent],
    entryComponents: [BulkUploadComponent, BulkUploadAddComponent],
    providers: [FileuploadService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadBulkUploadModule {}
