import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { TrailUpfrontPayState } from './';
import { TrailUpfrontPayComponent } from './trailupfrontpay/trailupfrontpay.component';
import { TrailUpfrontPayService } from './trailupfrontpay.service';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgbModule,
        RouterModule.forChild(TrailUpfrontPayState),
        NgMultiSelectDropDownModule.forRoot(),
        BsDatepickerModule.forRoot(),
        FormsModule,
        CommonModule
    ],
    declarations: [TrailUpfrontPayComponent],
    entryComponents: [TrailUpfrontPayComponent],
    providers: [TrailUpfrontPayService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadTrailUpfrontPayModule {}
