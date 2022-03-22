import { Route } from '@angular/router';
import { SubscriptionComponent } from './subscription.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const SubscrptionRoutes: Route = {
    path: 'subscription',
    component: SubscriptionComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
