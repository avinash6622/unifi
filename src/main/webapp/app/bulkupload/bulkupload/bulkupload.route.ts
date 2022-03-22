import { Route } from '@angular/router';
import { BulkUploadComponent } from './bulkupload.component';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const bulkuploadRoutes: Route = {
    path: 'bulkupload',
    component: BulkUploadComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: ''
    }
};
