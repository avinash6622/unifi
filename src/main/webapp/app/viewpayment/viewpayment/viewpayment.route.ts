import { Route } from '@angular/router';
import { ViewPaymentComponent } from './viewpayment.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const ViewPaymentRoutes: Route = {
    path: 'viewpayment',
    component: ViewPaymentComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
