import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { distributorRoutes } from './distribution-option/distribution-option.route';

const DISTRIBUTION_ROUTES = [distributorRoutes];

export const distributionState: Routes = [
    {
        path: '',
        children: DISTRIBUTION_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'DistributorOption'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
