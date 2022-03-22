import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { SubRMRoutes } from './Sub-RM/Sub-RM.route';

const SUB_RM_ROUTES = [SubRMRoutes];

export const subRMState: Routes = [
    {
        path: '',
        children: SUB_RM_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
