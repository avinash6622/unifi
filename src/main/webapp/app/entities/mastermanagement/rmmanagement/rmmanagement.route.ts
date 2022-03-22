import { Route } from '@angular/router';
import { RmmanagementComponent } from './rmmanagement.component';

export const rmmgmtRoutes: Route = {
    path: 'rmmgmt',
    component: RmmanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'rmmgmt'
    }
};
