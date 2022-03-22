import { Route } from '@angular/router';
import { AIF2FeeComponent } from './aif2-management-fee.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const AIF2FeeRoutes: Route = {
    path: 'aif2-management-fee',
    component: AIF2FeeComponent,
    data: {
        authorities: ['ROLE_ADMIN', 'ROLE_USER'],
        canEnable: {
            enableFor: 'roleView',
            roleName: 'ManagementFee'
        }
    },
    canActivate: [UserRouteAccessService, PageRouteAccessService]
};
