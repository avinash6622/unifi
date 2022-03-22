import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';
import { clientcommissionsRoutes } from './client-commissions/client-commissions.route';
import { ClientcommissionUpdateRoutes } from './client-commission.update/client-commission-update.route';
const CLIENT_COMMISSION_ROUTES = [clientcommissionsRoutes, ClientcommissionUpdateRoutes];
export const clientcommissionState: Routes = [
    {
        path: '',
        children: CLIENT_COMMISSION_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
