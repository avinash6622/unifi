import { Route } from '@angular/router';
import { MastermanagementComponent } from './mastermanagement.component';

export const mastermgmtRoutes: Route = {
    path: 'mastermgmt',
    component: MastermanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'mastermanagement'
    }
};
