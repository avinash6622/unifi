import { Route } from '@angular/router';
import { PmsComponent } from './pms.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const PmsRoutes: Route = {
    path: 'pmsclients',
    component: PmsComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    }
};
