import { Route } from '@angular/router';
import { LocationmanagementComponent } from 'app/entities/mastermanagement/locationmanagement/locationmanagement.component';

export const locationmgmtRoutes: Route = {
    path: 'locationmgmt',
    component: LocationmanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'locationmgmt'
    }
};
