import { Route } from '@angular/router';
import { InvestmentComponent } from './investment.component';
import { UserRouteAccessService } from 'app/core';

export const investmentRoutes: Route = {
    path: 'investment',
    component: InvestmentComponent,
    data: {
        authorities: [],
        pagetitle: 'Investment',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
};
