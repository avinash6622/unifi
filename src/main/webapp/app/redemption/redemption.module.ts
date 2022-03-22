import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { redemptionState } from './';
import { RedemptionComponent } from './redemption/redemption.component';
import { RedemptionService } from './redemption.service';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgbModule,
        RouterModule.forChild(redemptionState),
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        BsDatepickerModule.forRoot(),
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [RedemptionComponent],
    entryComponents: [RedemptionComponent],
    providers: [RedemptionService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadRedemptionModule {}
