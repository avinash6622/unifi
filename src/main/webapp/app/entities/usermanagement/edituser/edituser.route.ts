import { Route, Routes } from '@angular/router';
import { EdituserComponent } from 'app/entities/usermanagement/edituser/edituser.component';

export const edituserRoute: Route = {
    path: 'edituser',
    component: EdituserComponent,
    data: {
        authorities: [],
        pagetitle: 'edituser'
    }
};
