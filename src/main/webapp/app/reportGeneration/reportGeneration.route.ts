import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { ReportGenerationRoutes } from './reportGeneration/reportGeneration.route';

const REPORTGENERATIONROUTES = [ReportGenerationRoutes];

export const ReportGenerationState: Routes = [
    {
        path: '',
        children: REPORTGENERATIONROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService]
    }
];
