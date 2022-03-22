import { PageRouteAccessService } from './../core/auth/page-route-access.service';
import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { cdRoutes } from './cd/cd.route';
import { cdAddRoutes } from './cd-add/cd-add.route';
import { cdUpdateRoutes } from './cd-update/cd-update.route';

const CD_ROUTES = [cdRoutes, cdAddRoutes, cdUpdateRoutes];

export const cdState: Routes = [
    {
        path: '',
        children: CD_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'CommissionDefinition'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
