import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { fileuploadRoutes } from './fileupload/fileupload.route';
import { uploadAddRoutes } from './upload-add/upload-add.route';
import { uploadAifAddRoutes } from './aifupload/aifupload-add/aifupload-add.route';
import { aifFileUploadRoutes } from './aifupload/aifupload.route';
import { FileUploadMasterRoutes } from './fileupload-master/fileupload-master.route';
import { UpfrontRoutes } from './upfront/upfront.route';
import { upfrontAddRoutes } from './upfront/upfront-add/upfront-add.route';
import { fileuploadmasterAddRoutes } from './fileupload-master/fileupload-master-add/fileupload-master-add.route';

const FILEUPLOAD_ROUTES = [
    fileuploadRoutes,
    uploadAddRoutes,
    uploadAifAddRoutes,
    upfrontAddRoutes,
    fileuploadmasterAddRoutes,
    aifFileUploadRoutes,
    FileUploadMasterRoutes,
    UpfrontRoutes
];

export const fileuploadState: Routes = [
    {
        path: '',
        children: FILEUPLOAD_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'FileUpload'
            }
        },
        canActivate: [UserRouteAccessService]
    }
];
