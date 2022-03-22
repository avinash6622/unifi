import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { clientmasterState } from './clientmaster-cd.route';
import { ClientmasterComponent } from './clientmaster-cd/clientmaster-cd.component';
import { ClientmasterAddComponent } from './clientmaster-cd-add/clientmaster-cd-add.component';
import { ClientmasterUpdateComponent } from './clientmaster-cd-update/clientmaster-cd-update.component';
import { PmsComponent } from './pms/pms.component';
import { AifComponent } from './aif/aif.component';
import { ClientmasterService } from './clientmaster-cd.service';
import { PmsService } from './pms/pms.service';
import { AifService } from './aif/aif.service';
import { PmsAddComponent } from './pms/pms-add/pms-add.component';
import { PmsUpdateComponent } from './pms/pms-update/pms-update.component';
import { AifAddComponent } from './aif/aif-add/aif-add.component';
import { AifUpdateComponent } from './aif/aif-update/aif-update.component';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
    imports: [
        NgxPaginationModule,
        RouterModule.forChild(clientmasterState),
        NgMultiSelectDropDownModule.forRoot(),
        TypeaheadModule.forRoot(),
        FormsModule,
        CommonModule,
        NgbModule,
        NgSelectModule
    ],
    declarations: [
        ClientmasterAddComponent,
        ClientmasterComponent,
        ClientmasterUpdateComponent,
        PmsComponent,
        AifComponent,
        PmsAddComponent,
        PmsUpdateComponent,
        AifAddComponent,
        AifUpdateComponent
    ],
    entryComponents: [
        ClientmasterComponent,
        ClientmasterAddComponent,
        ClientmasterUpdateComponent,
        PmsComponent,
        AifComponent,
        PmsAddComponent,
        PmsUpdateComponent,
        AifAddComponent,
        AifUpdateComponent
    ],
    exports: [ClientmasterAddComponent, ClientmasterUpdateComponent],
    providers: [ClientmasterService, PmsService, AifService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadClientmasterModule {}
