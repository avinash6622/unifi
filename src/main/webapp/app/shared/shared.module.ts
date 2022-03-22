import { HasAnyRoleDirective } from './auth/has-any-authority.directive';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { BcadSharedLibsModule, BcadSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [BcadSharedLibsModule, BcadSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, HasAnyRoleDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [BcadSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, HasAnyRoleDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadSharedModule {}
