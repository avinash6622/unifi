import { Route } from '@angular/router';
import { ClientCommissionUpdateComponent } from './client-commission-update.component';
import { UserRouteAccessService } from 'app/core';

export const ClientcommissionUpdateRoutes: Route = {
    path: 'client-comm/:id/edit',
    component: ClientCommissionUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientCommission'
    }
};
