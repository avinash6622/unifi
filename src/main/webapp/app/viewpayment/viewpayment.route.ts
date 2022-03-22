import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { ViewPaymentRoutes } from './viewpayment/viewpayment.route';

const VIEWPAYMENTROUTES = [ViewPaymentRoutes];

export const ViewPaymentState: Routes = [
    {
        path: '',
        children: VIEWPAYMENTROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
