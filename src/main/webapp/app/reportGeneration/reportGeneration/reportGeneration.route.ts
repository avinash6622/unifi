import { Route } from '@angular/router';
import { ReportGenerationComponent } from './reportGeneration.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const ReportGenerationRoutes: Route = {
    path: 'reportgeneration',
    component: ReportGenerationComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
