import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { ViewPaymentState } from './';
import { ViewPaymentComponent } from './viewpayment/viewpayment.component';
import { ViewPaymentService } from './viewpayment.service';
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
        RouterModule.forChild(ViewPaymentState),
        FormsModule,
        CommonModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [ViewPaymentComponent],
    entryComponents: [ViewPaymentComponent],
    providers: [ViewPaymentService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadViewPaymentModule {}
