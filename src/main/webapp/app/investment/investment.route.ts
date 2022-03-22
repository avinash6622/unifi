import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { investmentRoutes } from './investment/investment.route';

const INVESTMENT_ROUTES = [investmentRoutes];

export const investmentState: Routes = [
    {
        path: '',
        children: INVESTMENT_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'Investment'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
