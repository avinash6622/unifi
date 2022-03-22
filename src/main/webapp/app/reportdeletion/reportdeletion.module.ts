import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReportState } from './';
import { ReportDeletionComponent } from './reportdeletion/reportdeletion.component';
import { ReportDeletionService } from './reportdeletion.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BusyModule } from 'ngx-busy';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        BusyModule,
        NgbModule,
        NgxPaginationModule,
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(ReportState),
        FormsModule,
        CommonModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [ReportDeletionComponent],
    entryComponents: [ReportDeletionComponent],
    providers: [ReportDeletionService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadReportdeletionModule {}
