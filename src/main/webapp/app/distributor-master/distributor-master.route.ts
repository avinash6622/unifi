import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { distributorRoutes } from './distributor-master/distributor-master.route';
import { distributorAddRoutes } from './distributor-master-add/distributor-master-add.route';
import { distributorUpdateRoutes } from './distributor-master-update/distributor-master-update.route';

const DISTRIBUTOR_MASTER_ROUTES = [distributorRoutes, distributorAddRoutes, distributorUpdateRoutes];

export const distributorState: Routes = [
    {
        path: '',
        children: DISTRIBUTOR_MASTER_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
