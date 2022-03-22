import { Route } from '@angular/router';
import { UsermanagementComponent } from './usermanagement.component';

export const usermgmtRoutes: Route = {
    path: 'usermanagement',
    component: UsermanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'usermanagement'
    }
};
