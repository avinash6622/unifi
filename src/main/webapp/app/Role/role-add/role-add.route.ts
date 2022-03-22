import { Route } from '@angular/router';
import { RoleAddComponent } from './role-add.component';
import { UserRouteAccessService } from 'app/core';

export const roleAddRoutes: Route = {
    path: 'role-add',
    component: RoleAddComponent,
    data: {
        authorities: [],
        pagetitle: 'Role'
    }
};
