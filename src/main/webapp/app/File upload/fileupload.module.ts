import { AifUploadAddComponent } from './aifupload/aifupload-add/aifupload-add.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { fileuploadState } from './fileupload.route';
import { FileuploadComponent } from './fileupload/fileupload.component';
import { UploadAddComponent } from './upload-add/upload-add.component';
import { AidUploadComponent } from './aifupload/aifupload.component';
import { FileUploadMasterComponent } from './fileupload-master/fileupload-master.component';
import { FileuploadService } from './fileupload.service';
import { UpfrontComponent } from './upfront/upfront.component';
import { UpfrontAddComponent } from './upfront/upfront-add/upfront-add.component';
import { FileUploadMasterAddComponent } from './fileupload-master/fileupload-master-add/fileupload-master-add.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { BusyModule } from 'ngx-busy';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        BusyModule,
        NgxPaginationModule,
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(fileuploadState),
        FormsModule,
        CommonModule,
        NgbModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [
        FileuploadComponent,
        UploadAddComponent,
        AidUploadComponent,
        FileUploadMasterComponent,
        AifUploadAddComponent,
        UpfrontComponent,
        UpfrontAddComponent,
        FileUploadMasterAddComponent
    ],
    entryComponents: [
        FileuploadComponent,
        UploadAddComponent,
        AidUploadComponent,
        AifUploadAddComponent,
        UpfrontComponent,
        FileUploadMasterComponent,
        UpfrontAddComponent,
        FileUploadMasterAddComponent
    ],
    providers: [FileuploadService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadFileuploadModule {}
