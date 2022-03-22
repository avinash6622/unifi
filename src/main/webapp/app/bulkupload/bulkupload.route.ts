import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { bulkuploadRoutes } from './bulkupload/bulkupload.route';
import { bulkuploadAddRoutes } from './bulkupload-add/bulkupload-add.route';

const FILEUPLOAD_ROUTES = [bulkuploadRoutes, bulkuploadAddRoutes];

export const fileuploadState: Routes = [
    {
        path: '',
        children: FILEUPLOAD_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
