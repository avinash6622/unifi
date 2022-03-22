import { Route } from '@angular/router';
import { AIF2AddComponent } from './aif2-management-fee-add.component';
import { UserRouteAccessService } from 'app/core';

export const AIF2FeeAddRoutes: Route = {
    path: 'aif2-management-fee/add',
    component: AIF2AddComponent,
    data: {
        authorities: [],
        pagetitle: 'AIF2ManagementFee'
    }
};
