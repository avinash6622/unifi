import { Route } from '@angular/router';
import { UpfrontComponent } from './upfront.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const UpfrontRoutes: Route = {
    path: 'upfrontuploads',
    component: UpfrontComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    }
};
