import { Route } from '@angular/router';
import { UpfrontAddComponent } from './upfront-add.component';

export const upfrontAddRoutes: Route = {
    path: 'upfrontadd',
    component: UpfrontAddComponent,
    data: {
        authorities: [],
        pagetitle: 'upfrontadd'
    }
};
