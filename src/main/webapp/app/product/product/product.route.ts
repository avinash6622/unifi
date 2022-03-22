import { Route } from '@angular/router';
import { ProductComponent } from './product.component';
import { UserRouteAccessService } from 'app/core';

export const productRoutes: Route = {
    path: 'product',
    component: ProductComponent,
    data: {
        authorities: [],
        pagetitle: 'Product',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
};
