import { Route } from '@angular/router';
import { CdAddComponent } from './cd-add.component';
import { UserRouteAccessService } from 'app/core';

export const cdAddRoutes: Route = {
    path: 'cdadd',
    component: CdAddComponent,
    data: {
        authorities: [],
        pagetitle: 'CommissionDefinition'
    }
};
