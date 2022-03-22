import { Route } from '@angular/router';
import { ClientmasterComponent } from './clientmaster-cd.component';
import { UserRouteAccessService } from 'app/core';

export const clientmasterRoutes: Route = {
    path: 'Clientmaster',
    component: ClientmasterComponent,
    data: {
        authorities: [],
        pagetitle: 'Clientmaster'
    }
};
