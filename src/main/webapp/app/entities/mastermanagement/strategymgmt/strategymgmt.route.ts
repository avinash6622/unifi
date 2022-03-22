import { Route } from '@angular/router';
import { StrategymgmtComponent } from './strategymgmt.component';

export const strategymgmtRoutes: Route = {
    path: 'strategymgmt',
    component: StrategymgmtComponent,
    data: {
        authorities: [],
        pagetitle: 'strategymgmt'
    }
};
