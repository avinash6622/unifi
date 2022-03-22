import { Route } from '@angular/router';
import { FeemgmtComponent } from 'app/entities/mastermanagement/feemanagement/feemgmt.component';

export const feemgmtRoutes: Route = {
    path: 'feemgmt',
    component: FeemgmtComponent,
    data: {
        authorities: [],
        pagetitle: 'feemgmt'
    }
};
