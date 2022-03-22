import { Route } from '@angular/router';
import { PmsUpdateComponent } from './pms-update.component';
import { UserRouteAccessService } from 'app/core';

export const pmsUpdateRoutes: Route = {
    path: 'pms/:id/edit',
    component: PmsUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'Pms'
    }
};
