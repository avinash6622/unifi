import { UserRouteAccessService, PageRouteAccessService } from 'app/core';
import { Routes } from '@angular/router';
import { clientmasterRoutes } from './clientmaster-cd/clientmaster-cd.route';
import { ClientMasterAddRoutes } from './clientmaster-cd-add/clientmaster-cd-add.route';
import { ClientmasterUpdateRoutes } from './clientmaster-cd-update/clientmaster-cd-update.route';
import { PmsRoutes } from './pms/pms.route';
import { pmsAddRoutes } from './pms/pms-add/pms-add.route';
import { pmsUpdateRoutes } from './pms/pms-update/pms-update.route';
import { AifRoutes } from './aif/aif.route';
import { aifAddRoutes } from './aif/aif-add/aif-add.route';
import { aifUpdateRoutes } from './aif/aif-update/aif-update.route';

const CLIENTMASTER_ROUTES = [
    clientmasterRoutes,
    ClientMasterAddRoutes,
    ClientmasterUpdateRoutes,
    pmsAddRoutes,
    pmsUpdateRoutes,
    aifAddRoutes,
    aifUpdateRoutes,
    AifRoutes,
    PmsRoutes
];

export const clientmasterState: Routes = [
    {
        path: '',
        children: CLIENTMASTER_ROUTES,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            canEnable: {
                enableFor: 'roleView',
                roleName: 'ClientMaster'
            }
        },
        canActivate: [UserRouteAccessService, PageRouteAccessService]
    }
];
