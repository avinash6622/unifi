import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { clientfeecommissionState } from './clientfee-commission.route';
import { ClientFeeCommissionComponent } from './clientfee-commission/clientfee-commission.component';
import { ClientFeeCommissionUpdateComponent } from './clientfee-commission.update/clientfee-commission-update.component';
import { ClientFeeCommissionService } from './clientfee-commmission.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
    imports: [NgxPaginationModule, RouterModule.forChild(clientfeecommissionState), FormsModule, CommonModule, NgbModule, NgSelectModule],
    declarations: [ClientFeeCommissionComponent, ClientFeeCommissionUpdateComponent],
    entryComponents: [ClientFeeCommissionComponent, ClientFeeCommissionUpdateComponent],
    providers: [ClientFeeCommissionService],
    exports: [ClientFeeCommissionUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadClientFeeCommissionModule {}
