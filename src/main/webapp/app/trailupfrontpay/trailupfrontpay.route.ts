import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { TrailUpfrontPayRoutes } from './trailupfrontpay/trailupfrontpay.route';

const TRAILUPFRONTPAYROUTES = [TrailUpfrontPayRoutes];

export const TrailUpfrontPayState: Routes = [
    {
        path: '',
        children: TRAILUPFRONTPAYROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
