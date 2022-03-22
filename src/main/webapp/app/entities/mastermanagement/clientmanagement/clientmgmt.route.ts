import { Route } from '@angular/router';
import { ClientmgmtComponent } from './clientmgmt.component';

export const clientmgmtRoutes: Route = {
    path: 'clientmgmt',
    component: ClientmgmtComponent,
    data: {
        authorities: [],
        pagetitle: 'clientmgmt'
    }
};
