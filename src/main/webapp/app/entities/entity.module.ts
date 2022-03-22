import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MastermanagementComponent, PaymentComponent, UploadedComponent, UsermanagementComponent, entityState } from './';

import { AdduserComponent } from './usermanagement/adduser/adduser.component';
import { EdituserComponent } from './usermanagement/edituser/edituser.component';
import { changePasswordComponent } from './usermanagement/changePassword/changePassword.component';
import { ClientmgmtComponent } from 'app/entities/mastermanagement/clientmanagement/clientmgmt.component';
import { FeemgmtComponent } from 'app/entities/mastermanagement/feemanagement/feemgmt.component';
import { StrategymgmtComponent } from 'app/entities/mastermanagement/strategymgmt/strategymgmt.component';
import { RmmanagementComponent } from 'app/entities/mastermanagement/rmmanagement/rmmanagement.component';
import { SubrmmanagementComponent } from 'app/entities/mastermanagement/subrmmanagement/subrmmanagement.component';
import { CommissiondefinitionmgmtComponent } from './mastermanagement/commissiondefinitionmgmt/commissiondefinitionmgmt.component';
import { DisttypemanagementComponent } from 'app/entities/mastermanagement/disttypemanagement/disttypemanagement.component';
import { DistmanagementComponent } from 'app/entities/mastermanagement/distmanagement/distmanagement.component';
import { OptionmanagementComponent } from 'app/entities/mastermanagement/optionmanagement/optionmanagement.component';
import { LocationmanagementComponent } from './mastermanagement/locationmanagement/locationmanagement.component';
import { MasteruploadComponent } from './upload/masterupload/masterupload.component';
import { StrategyuploadComponent } from './upload/strategyupload/strategyupload.component';
@NgModule({
    imports: [RouterModule.forChild(entityState), FormsModule, CommonModule],
    declarations: [
        MastermanagementComponent,
        PaymentComponent,

        UploadedComponent,
        UsermanagementComponent,
        AdduserComponent,
        EdituserComponent,
        changePasswordComponent,
        ClientmgmtComponent,
        FeemgmtComponent,
        StrategymgmtComponent,
        RmmanagementComponent,
        SubrmmanagementComponent,
        CommissiondefinitionmgmtComponent,
        DisttypemanagementComponent,
        DistmanagementComponent,
        OptionmanagementComponent,
        LocationmanagementComponent,
        MasteruploadComponent,
        StrategyuploadComponent
    ],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadEntityModule {}
