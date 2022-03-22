import { Route } from '@angular/router';
import { DistributionOptionComponent } from './distribution-option.component';
import { UserRouteAccessService } from 'app/core';

export const distributorRoutes: Route = {
    path: 'distributor-option',
    component: DistributionOptionComponent,
    data: {
        authorities: [],
        pagetitle: 'Distributor'
    }
};
