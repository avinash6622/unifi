import { Route } from '@angular/router';
import { CdUpdateComponent } from './cd-update.component';
import { UserRouteAccessService } from 'app/core';

export const cdUpdateRoutes: Route = {
    path: 'cd/:id/edit',
    component: CdUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'CD'
    }
};
