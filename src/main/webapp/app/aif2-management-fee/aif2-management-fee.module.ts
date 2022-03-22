import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { AIF2FeeState } from './aif2-management-fee.route';
import { AIF2FeeComponent } from './aif2-management-fee/aif2-management-fee.component';
import { AIF2AddComponent } from './aif2-management-fee-add/aif2-management-fee-add.component';
import { Aif2UpdateComponent } from './aif2-management-fee-update/aif2-management-fee-update.component';
import { AIF2FeeService } from './aif2-management-fee.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgbModule,
        BsDatepickerModule.forRoot(),
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(AIF2FeeState),
        FormsModule,
        CommonModule,
        TypeaheadModule.forRoot()
    ],
    declarations: [AIF2FeeComponent, AIF2AddComponent, Aif2UpdateComponent],
    entryComponents: [AIF2FeeComponent, AIF2AddComponent, Aif2UpdateComponent],
    providers: [AIF2FeeService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadAIF2ManagementFeeModule {}
