import { NgModule } from '@angular/core';

import { BcadSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BcadSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BcadSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BcadSharedCommonModule {}
