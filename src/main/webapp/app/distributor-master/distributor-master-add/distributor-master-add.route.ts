import { Route } from '@angular/router';
import { DistributorMasterAddComponent } from './distributor-master-add.component';
import { UserRouteAccessService } from 'app/core';

export const distributorAddRoutes: Route = {
    path: 'distributor-master-add',
    component: DistributorMasterAddComponent,
    data: {
        authorities: [],
        pagetitle: 'DistributorMaster'
    }
};
