import { Route } from '@angular/router';
import { AidUploadComponent } from './aifupload.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const aifFileUploadRoutes: Route = {
    path: 'aifuploads',
    component: AidUploadComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    }
};
