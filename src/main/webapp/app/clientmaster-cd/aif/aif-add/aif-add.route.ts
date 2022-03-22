import { Route } from '@angular/router';
import { AifAddComponent } from './aif-add.component';

import { UserRouteAccessService } from 'app/core';

export const aifAddRoutes: Route = {
    path: 'aifadd',
    component: AifAddComponent,
    data: {
        authorities: [],
        pagetitle: 'Aif'
    }
};
