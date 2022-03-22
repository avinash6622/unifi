import { Route } from '@angular/router';
import { PaymentComponent } from './payment.component';

export const pmtRoutes: Route = {
    path: 'pmtroutes',
    component: PaymentComponent,
    data: {
        authorities: [],
        pagetitle: 'payment'
    }
};
