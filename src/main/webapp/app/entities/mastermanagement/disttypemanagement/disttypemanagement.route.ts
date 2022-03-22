import { Route } from '@angular/router';
import { DisttypemanagementComponent } from './disttypemanagement.component';

export const disttypemgmtRoutes: Route = {
    path: 'disttypemgmt',
    component: DisttypemanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'disttypemgmt'
    }
};
