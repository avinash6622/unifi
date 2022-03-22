import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { rmRoutes } from './rm/rm.route';
import { rmAddRoutes } from './rm-add/rm-add.route';
import { rmUpdateRoutes } from './rm-update/rm-update.route';

const RM_ROUTES = [rmRoutes, rmAddRoutes, rmUpdateRoutes];

export const rmState: Routes = [
    {
        path: '',
        children: RM_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'RM'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
