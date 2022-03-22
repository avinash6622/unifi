import { Route } from '@angular/router';
import { DistributorMasterComponent } from './distributor-master.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const distributorRoutes: Route = {
    path: 'distributor-master',
    component: DistributorMasterComponent,
    canActivate: [PageRouteAccessService],
    data: {
        authorities: [],
        pagetitle: 'DistributorMaster',
        canEnable: {
            enableFor: 'roleView',
            roleName: 'Distributor'
        }
    }
};
