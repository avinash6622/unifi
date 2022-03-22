import { Route, Routes } from '@angular/router';
import { AddroleComponent } from './addrole.component';

export const addroleRoutes: Route = {
    path: 'addrole',
    component: AddroleComponent,
    data: {
        authorities: [],
        pagetitle: 'addrole'
    }
};
