import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { roleState } from './role.route';
import { RoleComponent } from './role/role.component';
import { RoleAddComponent } from './role-add/role-add.component';
import { RoleUpdateComponent } from './role-update/role-update.component';
import { RoleService } from './role.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(roleState), FormsModule, CommonModule],
    declarations: [RoleComponent, RoleAddComponent, RoleUpdateComponent],
    entryComponents: [RoleComponent, RoleAddComponent, RoleUpdateComponent],
    providers: [RoleService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadRoleModule {}
