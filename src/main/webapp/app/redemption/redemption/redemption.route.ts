import { Route } from '@angular/router';
import { RedemptionComponent } from './redemption.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const RedemptionRoutes: Route = {
    path: 'redemption',
    component: RedemptionComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
