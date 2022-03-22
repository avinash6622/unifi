import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { clientfeecommissionRoutes } from './clientfee-commission/clientfee-commission.route';
import { ClientfeecommissionUpdateRoutes } from './clientfee-commission.update/clientfee-commission-update.route';
const CLIENTFEE_COMMISSION_ROUTES = [clientfeecommissionRoutes, ClientfeecommissionUpdateRoutes];

export const clientfeecommissionState: Routes = [
    {
        path: '',
        children: CLIENTFEE_COMMISSION_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
