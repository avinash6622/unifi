import { Routes } from '@angular/router';
import { auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, metricsRoute, trackerRoute, userMgmtRoute } from './';
import { PageRouteAccessService } from './../core/auth/page-route-access.service';
import { UserRouteAccessService } from './../core/auth/user-route-access-service';

const ADMIN_ROUTES = [auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, trackerRoute, ...userMgmtRoute, metricsRoute];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'User'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService],
        children: ADMIN_ROUTES
    }
];
