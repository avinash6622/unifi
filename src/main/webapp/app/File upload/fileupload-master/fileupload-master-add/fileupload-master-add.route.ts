import { Route } from '@angular/router';

import { FileUploadMasterAddComponent } from './fileupload-master-add.component';

export const fileuploadmasterAddRoutes: Route = {
    path: 'fileuploadmasteradd',
    component: FileUploadMasterAddComponent,
    data: {
        authorities: [],
        pagetitle: 'fileuploadmasteradd'
    }
};
