import { Route } from '@angular/router';
import { ClientFeeCommissionUpdateComponent } from './clientfee-commission-update.component';
import { UserRouteAccessService } from 'app/core';

export const ClientfeecommissionUpdateRoutes: Route = {
    path: 'client-fee/:id/edit',
    component: ClientFeeCommissionUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientFeeCommission'
    }
};
