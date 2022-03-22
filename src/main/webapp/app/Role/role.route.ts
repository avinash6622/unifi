import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { roleRoutes } from './role/role.route';
import { roleAddRoutes } from './role-add/role-add.route';
import { roleUpdateRoutes } from './role-update/role-update.route';

const ROLE_MASTER_ROUTES = [roleRoutes, roleAddRoutes, roleUpdateRoutes];

export const roleState: Routes = [
    {
        path: '',
        children: ROLE_MASTER_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
