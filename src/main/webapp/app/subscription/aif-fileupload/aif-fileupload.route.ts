import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { AifUploadComponent } from './aif-fileupload.component';

export const aifuploadAddRoutes: Route = {
    path: 'aifupload',
    component: AifUploadComponent,
    data: {
        authorities: [],
        pagetitle: 'AifUpload'
    }
};
