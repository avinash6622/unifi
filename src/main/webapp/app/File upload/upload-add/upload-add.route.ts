import { Route } from '@angular/router';
import { UploadAddComponent } from './upload-add.component';
import { UserRouteAccessService } from 'app/core';

export const uploadAddRoutes: Route = {
    path: 'uploadadd',
    component: UploadAddComponent,
    data: {
        authorities: [],
        pagetitle: 'UploadAdd'
    }
};
