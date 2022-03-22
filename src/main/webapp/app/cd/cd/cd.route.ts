import { Route } from '@angular/router';
import { CdComponent } from './cd.component';
import { UserRouteAccessService, PageRouteAccessService } from 'app/core';

export const cdRoutes: Route = {
    path: 'cd',
    component: CdComponent,
    canActivate: [PageRouteAccessService]
};
