import { Route } from '@angular/router';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { SeriesComponent } from './series.component';

export const seriesRoutes: Route = {
    path: 'series',
    component: SeriesComponent,
    data: {
        authorities: ['ROLE_ADMIN', 'ROLE_USER'],
        canEnable: {
            enableFor: 'roleView',
            roleName: 'Series'
        }
    },
    canActivate: [UserRouteAccessService, PageRouteAccessService]
};
