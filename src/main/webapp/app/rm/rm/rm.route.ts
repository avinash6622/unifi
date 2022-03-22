import { Route } from '@angular/router';
import { RmComponent } from './rm.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const rmRoutes: Route = {
    path: 'rm',
    component: RmComponent,
    data: {
        authorities: ['ROLE_ADMIN', 'ROLE_USER'],
        canEnable: {
            enableFor: 'roleView',
            roleName: 'RM'
        }
    },
    canActivate: [UserRouteAccessService, PageRouteAccessService]
};
