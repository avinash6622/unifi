import { Route } from '@angular/router';
import { SubRMComponent } from './Sub-RM.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const SubRMRoutes: Route = {
    path: 'sub-rm',
    component: SubRMComponent,
    data: {
        authorities: [],
        pagetitle: 'Sub-RM',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'SubRM'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
};
