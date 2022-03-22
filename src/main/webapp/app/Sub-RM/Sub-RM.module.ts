import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { subRMState } from './';
import { SubRMComponent } from './Sub-RM/Sub-RM.component';
import { SubRMAddComponent } from './Sub-RM-add/Sub-RM-add.component';
import { SubRMUpdateComponent } from './Sub-RM-update/Sub-RM.update.component';
import { SubRMService } from './Sub-RM.service';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(subRMState), FormsModule, CommonModule, TypeaheadModule.forRoot()],
    declarations: [SubRMComponent, SubRMAddComponent, SubRMUpdateComponent],
    entryComponents: [SubRMComponent, SubRMAddComponent, SubRMUpdateComponent],
    providers: [SubRMService],
    exports: [SubRMAddComponent, SubRMUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadSubRMModule {}
