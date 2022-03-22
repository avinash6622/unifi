import { Route } from '@angular/router';
import { AifComponent } from './aif.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const AifRoutes: Route = {
    path: 'aifclients',
    component: AifComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    }
};
