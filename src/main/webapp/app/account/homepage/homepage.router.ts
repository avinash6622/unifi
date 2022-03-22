import { Route } from '@angular/router';
import { HomepageComponent } from './homepage.component';

export const loginformRoute: Route = {
    path: 'homepage',
    component: HomepageComponent,
    data: {
        authorities: [],
        pageTitle: 'home'
    }
};
