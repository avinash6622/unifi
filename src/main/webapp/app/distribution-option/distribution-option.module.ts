import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { distributionState } from './';

import { DistributionOptionComponent } from './distribution-option/distribution-option.component';
import { DistributionOptionAddComponent } from './distribution-option-add/distribution-option-add.component';
import { DistributionOptionUpdateComponent } from './distribution-option-update/distribution-option-update.component';
import { DistributionService } from './distribution-option.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(distributionState), FormsModule, CommonModule],
    declarations: [DistributionOptionAddComponent, DistributionOptionComponent, DistributionOptionUpdateComponent],
    entryComponents: [DistributionOptionComponent, DistributionOptionAddComponent, DistributionOptionUpdateComponent],
    exports: [DistributionOptionAddComponent, DistributionOptionUpdateComponent],
    providers: [DistributionService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadDistributionOptionModule {}
