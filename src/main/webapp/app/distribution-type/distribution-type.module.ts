import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { distributiontypeState } from './';

import { DistributionTypeComponent } from './distribution-type/distribution-type.component';
import { DistributionTypeAddComponent } from './distribution-type-add/distribution-type-add.component';
import { DistributionTypeUpdateComponent } from './distribution-type-update/distribution-type-update.component';
import { DistributionTypeService } from './distribution-type.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(distributiontypeState), FormsModule, CommonModule],
    declarations: [DistributionTypeAddComponent, DistributionTypeComponent, DistributionTypeUpdateComponent],
    entryComponents: [DistributionTypeComponent, DistributionTypeAddComponent, DistributionTypeUpdateComponent],
    exports: [DistributionTypeAddComponent, DistributionTypeUpdateComponent],
    providers: [DistributionTypeService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadDistributionTypeModule {}
