import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { SubscrptionRoutes } from './subscription/subscription.route';
import { aifuploadAddRoutes } from './aif-fileupload/aif-fileupload.route';

const SUBSCRIPTIONROUTES = [SubscrptionRoutes, aifuploadAddRoutes];

export const subscriptionState: Routes = [
    {
        path: '',
        children: SUBSCRIPTIONROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
