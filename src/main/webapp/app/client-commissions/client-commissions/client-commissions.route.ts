import { Route } from '@angular/router';
import { ClientCommissionsComponent } from './client-commissions.component';
import { UserRouteAccessService } from 'app/core';

export const clientcommissionsRoutes: Route = {
    path: 'client-Commission',
    component: ClientCommissionsComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientCommission'
    }
};
