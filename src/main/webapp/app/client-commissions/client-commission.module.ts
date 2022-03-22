import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { clientcommissionState } from './client-commission.route';
import { ClientCommissionsComponent } from './client-commissions/client-commissions.component';
import { ClientCommissionUpdateComponent } from './client-commission.update/client-commission-update.component';
import { ClientCommissionService } from './client-commission.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
    imports: [NgxPaginationModule, RouterModule.forChild(clientcommissionState), FormsModule, CommonModule, NgbModule, NgSelectModule],
    declarations: [ClientCommissionsComponent, ClientCommissionUpdateComponent],
    entryComponents: [ClientCommissionsComponent, ClientCommissionUpdateComponent],
    providers: [ClientCommissionService],
    exports: [ClientCommissionUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClientCommissionModule {}
