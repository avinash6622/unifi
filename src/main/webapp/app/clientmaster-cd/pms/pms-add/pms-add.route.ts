import { Route } from '@angular/router';
import { PmsAddComponent } from './pms-add.component';

import { UserRouteAccessService } from 'app/core';

export const pmsAddRoutes: Route = {
    path: 'pmsadd',
    component: PmsAddComponent,
    data: {
        authorities: [],
        pagetitle: 'Pms'
    }
};
