import { Route } from '@angular/router';
import { TrailUpfrontPayComponent } from './trailupfrontpay.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const TrailUpfrontPayRoutes: Route = {
    path: 'trailupfrontpay',
    component: TrailUpfrontPayComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
