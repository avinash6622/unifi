import { Route } from '@angular/router';
import { FileuploadComponent } from './fileupload.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const fileuploadRoutes: Route = {
    path: 'uploads',
    component: FileuploadComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        defaultSort: 'id,asc',
        pagetitle: '',
        canEnable: {
            enableFor: 'roleView',
            roleName: 'FileUpload'
        }
    }
};
