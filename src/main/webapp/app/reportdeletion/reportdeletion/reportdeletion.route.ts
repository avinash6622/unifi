import { Route } from '@angular/router';
import { ReportDeletionComponent } from './reportdeletion.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const ReportRoutes: Route = {
    path: 'reportdeletion',
    component: ReportDeletionComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
