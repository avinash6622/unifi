import { Route } from '@angular/router';
import { Aif2UpdateComponent } from './aif2-management-fee-update.component';
import { UserRouteAccessService } from 'app/core';

export const AIF2FeeUpdateRoutes: Route = {
    path: 'aif2-management-fee/:id/edit',
    component: Aif2UpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'AIF2ManagementFee'
    }
};
