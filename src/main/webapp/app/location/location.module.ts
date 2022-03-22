import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { locationState } from './';

import { LocationComponent } from './location/location.component';
import { LocationAddComponent } from './location-add/location-add.component';
import { LocationUpdateComponent } from './location-update/location-update.component';
import { LocationService } from './location.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(locationState), FormsModule, CommonModule],
    declarations: [LocationAddComponent, LocationComponent, LocationUpdateComponent],
    entryComponents: [LocationComponent, LocationAddComponent, LocationUpdateComponent],
    exports: [LocationAddComponent, LocationUpdateComponent],
    providers: [LocationService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadLocationModule {}
