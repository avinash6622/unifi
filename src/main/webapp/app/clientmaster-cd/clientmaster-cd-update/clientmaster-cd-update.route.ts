import { Route } from '@angular/router';
import { ClientmasterUpdateComponent } from './clientmaster-cd-update.component';
import { UserRouteAccessService } from 'app/core';

export const ClientmasterUpdateRoutes: Route = {
    path: 'client-master/:id/edit',
    component: ClientmasterUpdateComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientMaster'
    }
};
