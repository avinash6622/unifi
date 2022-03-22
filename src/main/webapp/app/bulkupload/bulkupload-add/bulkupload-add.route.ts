import { Route } from '@angular/router';
import { BulkUploadAddComponent } from './bulkupload-add.component';
import { UserRouteAccessService } from 'app/core';

export const bulkuploadAddRoutes: Route = {
    path: 'bulkuploadadd',
    component: BulkUploadAddComponent,
    data: {
        authorities: [],
        pagetitle: 'BulkUploadAdd'
    }
};
