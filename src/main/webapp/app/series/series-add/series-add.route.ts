import { Route } from '@angular/router';
import { SeriesAddComponent } from './series-add.component';
import { UserRouteAccessService } from 'app/core';

export const seriesAddRoutes: Route = {
    path: 'seriesadd',
    component: SeriesAddComponent,
    data: {
        authorities: [],
        pagetitle: 'series'
    }
};
