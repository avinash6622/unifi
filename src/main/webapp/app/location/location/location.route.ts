import { Route } from '@angular/router';
import { LocationComponent } from './location.component';
import { UserRouteAccessService } from 'app/core';

export const locationRoutes: Route = {
    path: 'location',
    component: LocationComponent,
    data: {
        authorities: [],
        pagetitle: 'Location',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
};
