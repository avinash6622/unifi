import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { seriesRoutes } from './series/series.route';
import { seriesAddRoutes } from './series-add/series-add.route';
import { seriesUpdateRoutes } from './series-update/series-update.route';

const series_ROUTES = [seriesRoutes, seriesAddRoutes, seriesUpdateRoutes];

export const seriesState: Routes = [
    {
        path: '',
        children: series_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
