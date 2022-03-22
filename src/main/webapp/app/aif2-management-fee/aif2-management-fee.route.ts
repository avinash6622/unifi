import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { AIF2FeeRoutes } from './aif2-management-fee/aif2-management-fee.route';
import { AIF2FeeAddRoutes } from './aif2-management-fee-add/aif2-management-fee-add.route';
import { AIF2FeeUpdateRoutes } from './aif2-management-fee-update/aif2-management-fee-update.route';

const AIF2FEE_ROUTES = [AIF2FeeRoutes, AIF2FeeAddRoutes, AIF2FeeUpdateRoutes];

export const AIF2FeeState: Routes = [
    {
        path: '',
        children: AIF2FEE_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
