import { Route } from '@angular/router';
import { OptionmanagementComponent } from './optionmanagement.component';

export const optionmgmtRoutes: Route = {
    path: 'optionmgmt',
    component: OptionmanagementComponent,
    data: {
        authorities: [],
        pagetitle: 'optionmgmt'
    }
};
