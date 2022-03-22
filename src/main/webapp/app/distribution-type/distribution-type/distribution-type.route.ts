import { Route } from '@angular/router';
import { DistributionTypeComponent } from './distribution-type.component';
import { UserRouteAccessService } from 'app/core';

export const distributortypeRoutes: Route = {
    path: 'distributor-type',
    component: DistributionTypeComponent,
    data: {
        authorities: [],
        pagetitle: 'Distributor Type',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
};
