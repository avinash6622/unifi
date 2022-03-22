import { Route, Routes } from '@angular/router';
import { AdduserComponent } from './adduser.component';

export const adduserRoutes: Route = {
    path: 'adduser',
    component: AdduserComponent,
    data: {
        authorities: [],
        pagetitle: 'adduser'
    }
};
