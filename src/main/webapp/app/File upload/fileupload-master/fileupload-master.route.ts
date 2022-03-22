import { Route } from '@angular/router';
import { FileUploadMasterComponent } from './fileupload-master.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const FileUploadMasterRoutes: Route = {
    path: 'masteruploads',
    component: FileUploadMasterComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    }
};
