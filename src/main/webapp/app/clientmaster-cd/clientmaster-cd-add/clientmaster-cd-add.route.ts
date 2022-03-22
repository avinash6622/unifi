import { Route } from '@angular/router';
import { ClientmasterAddComponent } from './clientmaster-cd-add.component';
import { UserRouteAccessService } from 'app/core';

export const ClientMasterAddRoutes: Route = {
    path: 'client-master-add',
    component: ClientmasterAddComponent,
    data: {
        authorities: [],
        pagetitle: 'ClientMaster'
    }
};
