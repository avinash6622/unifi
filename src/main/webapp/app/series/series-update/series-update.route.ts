import { Route } from '@angular/router';
import { SeriesUpdateComponent } from './series-update.component';
import { UserRouteAccessService } from 'app/core';

export const seriesUpdateRoutes: Route = {
    path: 'series/:id/edit',
    component: SeriesUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'Series'
    }
};
