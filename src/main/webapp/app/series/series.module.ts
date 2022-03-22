import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { seriesState } from './series.route';
import { SeriesComponent } from './series/series.component';
import { SeriesAddComponent } from './series-add/series-add.component';
import { SeriesUpdateComponent } from './series-update/series-update.component';
import { SeriesService } from './series.service';
import { AIF2Service } from './aif2-series-master/aif2-series-master.service';
import { AIF2Component } from './aif2-series-master/aif2-series-master/aif2-series-master.component';
import { Aif2AddComponent } from './aif2-series-master/aif2-series-master-add/aif2-series-master-add.component';
import { Aif2UpdateComponent } from './aif2-series-master/aif2-series-master-update/aif2-series-master-update.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(seriesState), FormsModule, CommonModule],
    declarations: [SeriesAddComponent, SeriesComponent, SeriesUpdateComponent, AIF2Component, Aif2AddComponent, Aif2UpdateComponent],
    entryComponents: [SeriesComponent, SeriesAddComponent, SeriesUpdateComponent, AIF2Component, Aif2AddComponent, Aif2UpdateComponent],
    exports: [Aif2AddComponent, Aif2UpdateComponent],
    providers: [SeriesService, AIF2Service],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadSeriesModule {}
