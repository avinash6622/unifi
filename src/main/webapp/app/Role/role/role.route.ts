import { Route } from '@angular/router';
import { RoleComponent } from './role.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const roleRoutes: Route = {
    path: 'roles',
    component: RoleComponent,
    data: {
        authorities: ['ROLE_ADMIN', 'ROLE_USER'],
        canEnable: {
            enableFor: 'roleView',
            roleName: 'DistType'
        }
    },
    canActivate: [UserRouteAccessService, PageRouteAccessService]
};
