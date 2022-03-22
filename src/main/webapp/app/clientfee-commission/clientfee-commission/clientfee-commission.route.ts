import { Route } from '@angular/router';
import { ClientFeeCommissionComponent } from './clientfee-commission.component';
import { UserRouteAccessService } from 'app/core';

export const clientfeecommissionRoutes: Route = {
    path: 'clientfee-Commission',
    component: ClientFeeCommissionComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientFeeCommission'
    }
};
