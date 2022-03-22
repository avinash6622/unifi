import { Routes } from '@angular/router';

import { mastermgmtRoutes, pmtRoutes, uploadedRoutes, usermgmtRoutes } from './';
import { addroleRoutes } from './usermanagement/addrole/addrole.routes';
import { adduserRoutes } from './usermanagement/adduser/adduser.route';
import { edituserRoute } from './usermanagement/edituser/edituser.route';
import { changePasswordRoutes } from './usermanagement/changePassword/changePassword.routes';
import { clientmgmtRoutes } from './mastermanagement/clientmanagement/clientmgmt.route';
import { feemgmtRoutes } from 'app/entities/mastermanagement/feemanagement/feemgmt.route';
import { strategymgmtRoutes } from 'app/entities/mastermanagement/strategymgmt/strategymgmt.route';
import { rmmgmtRoutes } from 'app/entities/mastermanagement/rmmanagement/rmmanagement.route';
import { subrmmgmtRoutes } from './mastermanagement/subrmmanagement/subrmmanagement.route';
import { commissiondefinitionmgmtRoutes } from './mastermanagement/commissiondefinitionmgmt/commissiondefinitionmgmt.route';
import { disttypemgmtRoutes } from 'app/entities/mastermanagement/disttypemanagement/disttypemanagement.route';
import { distmgmtRoutes } from 'app/entities/mastermanagement/distmanagement/distmanagement.route';
import { optionmgmtRoutes } from './mastermanagement/optionmanagement/optionmanagement.route';
import { locationmgmtRoutes } from 'app/entities/mastermanagement/locationmanagement/locationmanagement.route';
// import { masteruploadRoutes } from 'app/entities/upload/masterupload/masterupload.routes';
// import { strategyuploadRoutes } from 'app/entities/upload/strategyupload/strategyupload.routes';
import { from } from 'rxjs';

const ENTITY_ROUTES = [
    mastermgmtRoutes,
    pmtRoutes,
    uploadedRoutes,
    usermgmtRoutes,
    adduserRoutes,
    edituserRoute,
    changePasswordRoutes,
    clientmgmtRoutes,
    feemgmtRoutes,
    strategymgmtRoutes,
    rmmgmtRoutes,
    subrmmgmtRoutes,
    commissiondefinitionmgmtRoutes,
    disttypemgmtRoutes,
    distmgmtRoutes,
    optionmgmtRoutes,
    locationmgmtRoutes
    // masteruploadRoutes,
    // strategyuploadRoutes
];

export const entityState: Routes = [
    {
        path: '',
        children: ENTITY_ROUTES
    }
];
