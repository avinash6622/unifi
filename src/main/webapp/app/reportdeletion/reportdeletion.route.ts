import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { ReportRoutes } from './reportdeletion/reportdeletion.route';

const REPORTDELETIONROUTES = [ReportRoutes];

export const ReportState: Routes = [
    {
        path: '',
        children: REPORTDELETIONROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
