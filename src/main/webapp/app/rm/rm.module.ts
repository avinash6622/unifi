import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { rmState } from './rm.route';
import { RmComponent } from './rm/rm.component';
import { RmAddComponent } from './rm-add/rm-add.component';
import { RmUpdateComponent } from './rm-update/rm-update.component';
import { RmService } from './rm.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [
        NgxPaginationModule,
        NgbModule,
        NgMultiSelectDropDownModule.forRoot(),
        RouterModule.forChild(rmState),
        FormsModule,
        CommonModule,
        TypeaheadModule.forRoot()
    ],
    declarations: [RmComponent, RmAddComponent, RmUpdateComponent],
    entryComponents: [RmComponent, RmAddComponent, RmUpdateComponent],
    providers: [RmService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadRmModule {}
