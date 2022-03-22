import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { subscriptionState } from './';
import { SubscriptionComponent } from './subscription/subscription.component';
import { SubscriptionService } from './subscription.service';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AifUploadService } from './aif-fileupload/aifupload.service';
import { AifUploadComponent } from './aif-fileupload/aif-fileupload.component';
import { BusyModule } from 'ngx-busy';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgbModule,
        RouterModule.forChild(subscriptionState),
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        BusyModule,
        BsDatepickerModule.forRoot(),
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [SubscriptionComponent, AifUploadComponent],
    entryComponents: [SubscriptionComponent],
    providers: [SubscriptionService, AifUploadService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadSubscriptionModule {}
