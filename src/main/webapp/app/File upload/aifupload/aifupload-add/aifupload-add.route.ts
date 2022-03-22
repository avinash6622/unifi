import { Route } from '@angular/router';
import { AifUploadAddComponent } from './aifupload-add.component';

export const uploadAifAddRoutes: Route = {
    path: 'aifuploadadd',
    component: AifUploadAddComponent,
    data: {
        authorities: [],
        pagetitle: 'aifuploadadd'
    }
};
