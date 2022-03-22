import { Route } from '@angular/router';
import { UploadedComponent } from './uploaded.component';

export const uploadedRoutes: Route = {
    path: 'uploadedroutes',
    component: UploadedComponent,
    data: {
        authorities: [],
        pagetitle: 'uploaded'
    }
};
