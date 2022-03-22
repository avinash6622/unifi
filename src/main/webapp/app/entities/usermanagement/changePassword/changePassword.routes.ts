import { Route } from '@angular/router';
import { changePasswordComponent } from 'app/entities/usermanagement/changePassword/changePassword.component';

export const changePasswordRoutes: Route = {
    path: 'changePassword',
    component: changePasswordComponent,
    data: {
        authorities: [],
        pagetitle: 'changePassword'
    }
};
