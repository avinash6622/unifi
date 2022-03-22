import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { locationRoutes } from './location/location.route';

const LOCATION_ROUTES = [locationRoutes];

export const locationState: Routes = [
    {
        path: '',
        children: LOCATION_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'Location'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
