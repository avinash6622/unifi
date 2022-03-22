import { Route } from '@angular/router';
import { DistmanagementComponent } from 'app/entities/mastermanagement/distmanagement/distmanagement.component';

export const distmgmtRoutes: Route = {
    path: 'distmgmt',
    component: DistmanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'distmgmt'
    }
};
