import { Route } from '@angular/router';
import { DistributorMasterUpdateComponent } from './distributor-master-update.component';
import { UserRouteAccessService } from 'app/core';

export const distributorUpdateRoutes: Route = {
    path: 'distributor-master/:id/edit',
    component: DistributorMasterUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'DistributorMaster'
    }
};
