import { Route } from '@angular/router';
import { RmUpdateComponent } from './rm-update.component';
import { UserRouteAccessService } from 'app/core';

export const rmUpdateRoutes: Route = {
    path: 'rm/:id/edit',
    component: RmUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'Rm'
    }
};
