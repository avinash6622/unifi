import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReportGenerationState } from './';
import { ReportGenerationComponent } from './reportGeneration/reportGeneration.component';
import { ReportGenerationService } from './reportGeneration.service';
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
        RouterModule.forChild(ReportGenerationState),
        FormsModule,
        CommonModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [ReportGenerationComponent],
    entryComponents: [ReportGenerationComponent],
    providers: [ReportGenerationService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadReportGenerationModule {}
