import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';

import { productRoutes } from './product/product.route';

const PRODUCT_ROUTES = [productRoutes];

export const productState: Routes = [
    {
        path: '',
        children: PRODUCT_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'Product'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
