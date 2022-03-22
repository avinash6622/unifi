import { Route } from '@angular/router';
import { AifUpdateComponent } from './aif-update.component';
import { UserRouteAccessService } from 'app/core';

export const aifUpdateRoutes: Route = {
    path: 'aif/:id/edit',
    component: AifUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'Aif'
    }
};
