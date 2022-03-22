import { Route } from '@angular/router';
import { SubrmmanagementComponent } from 'app/entities/mastermanagement/subrmmanagement/subrmmanagement.component';

export const subrmmgmtRoutes: Route = {
    path: 'subrmmgmt',
    component: SubrmmanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'subrmmgmt'
    }
};
