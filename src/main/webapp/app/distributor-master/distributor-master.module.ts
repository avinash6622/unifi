import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { distributorState } from './distributor-master.route';
import { DistributorMasterComponent } from './distributor-master/distributor-master.component';
import { DistributorMasterAddComponent } from './distributor-master-add/distributor-master-add.component';
import { DistributorMasterUpdateComponent } from './distributor-master-update/distributor-master-update.component';
import { DistributorMasterService } from './distributor-master.service';
import { OrderModule } from 'ngx-order-pipe';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
    imports: [
        OrderModule,
        NgbModule,
        NgMultiSelectDropDownModule.forRoot(),
        NgxPaginationModule,
        RouterModule.forChild(distributorState),
        FormsModule,
        CommonModule,
        NgSelectModule,
        TypeaheadModule.forRoot()
    ],
    declarations: [DistributorMasterComponent, DistributorMasterAddComponent, DistributorMasterUpdateComponent],
    entryComponents: [DistributorMasterComponent, DistributorMasterAddComponent, DistributorMasterUpdateComponent],
    providers: [DistributorMasterService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadDistributorMasterModule {}
