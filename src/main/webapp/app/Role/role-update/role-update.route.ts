import { Route } from '@angular/router';
import { RoleUpdateComponent } from './role-update.component';
import { UserRouteAccessService } from 'app/core';

export const roleUpdateRoutes: Route = {
    path: 'role/:id/edit',
    component: RoleUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'RoleUpdate'
    }
};
