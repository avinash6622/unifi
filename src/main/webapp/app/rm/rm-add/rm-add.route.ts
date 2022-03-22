import { Route } from '@angular/router';
import { RmAddComponent } from './rm-add.component';
import { UserRouteAccessService } from 'app/core';

export const rmAddRoutes: Route = {
    path: 'rmadd',
    component: RmAddComponent,
    data: {
        authorities: [],
        pagetitle: 'Rm'
    }
};
