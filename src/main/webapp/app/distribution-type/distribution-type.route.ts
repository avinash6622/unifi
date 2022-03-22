import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { distributortypeRoutes } from './distribution-type/distribution-type.route';

const DISTRIBUTIONTYPE_ROUTES = [distributortypeRoutes];

export const distributiontypeState: Routes = [
    {
        path: '',
        children: DISTRIBUTIONTYPE_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'DistType'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
