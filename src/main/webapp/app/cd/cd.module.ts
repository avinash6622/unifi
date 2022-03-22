import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { cdState } from './cd.route';
import { CdComponent } from './cd/cd.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { CdAddComponent } from './cd-add/cd-add.component';
import { CdUpdateComponent } from './cd-update/cd-update.component';
import { CdService } from './cd.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(cdState),
        FormsModule,
        CommonModule,
        NgbModule,
        BsDatepickerModule.forRoot()
    ],
    declarations: [CdComponent, CdAddComponent, CdUpdateComponent],
    entryComponents: [CdComponent, CdAddComponent, CdUpdateComponent],
    providers: [CdService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadCdModule {}
