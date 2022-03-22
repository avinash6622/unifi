import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { RedemptionRoutes } from './redemption/redemption.route';

const redemptionROUTES = [RedemptionRoutes];

export const redemptionState: Routes = [
    {
        path: '',
        children: redemptionROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
