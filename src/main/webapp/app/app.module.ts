import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';
import { FormsModule } from '@angular/forms';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { BcadSharedModule } from 'app/shared';
import { BcadCoreModule } from 'app/core';
import { BcadAppRoutingModule } from './app-routing.module';
import { BcadHomeModule } from './home/home.module';
import { BcadLocationModule } from './location/location.module';
import { BcadSubRMModule } from './Sub-RM/Sub-RM.module';
import { BcadProductModule } from './product/product.module';
import { BcadClientmasterModule } from './clientmaster-cd/clientmaster-cd.module';
import { BcadClientFeeCommissionModule } from './clientfee-commission/clientfee-commission.module';
import { BcadDistributionOptionModule } from './distribution-option/distribution-option.module';
import { BcadDistributionTypeModule } from './distribution-type/distribution-type.module';
import { BcadDistributorMasterModule } from './distributor-master/distributor-master.module';
import { BcadInvestmentModule } from './investment/investment.module';
import { BcadRmModule } from './rm/rm.module';
import { BcadRoleModule } from './role/role.module';
import { BcadSeriesModule } from './series/series.module';
import { BcadCdModule } from './cd/cd.module';
import { BcadTrailUpfrontPayModule } from './trailupfrontpay/trailupfrontpay.module';
import { BcadReportGenerationModule } from './reportGeneration/reportGeneration.module';
import { BcadAccountModule } from './account/account.module';
import { BcadEntityModule } from './entities/entity.module';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { BcadFileuploadModule } from './File upload/fileupload.module';
// import { BcadfileUploadModule } from './file-upload/file-upload.module';
import { BcadBulkUploadModule } from './bulkupload/bulkupload.module';
// import { BcadAif2SeriesMasterModule } from './aif2-series-master/aif2-series-master.module';
import { BcadAIF2ManagementFeeModule } from './aif2-management-fee/aif2-management-fee.module';
import { BcadSubscriptionModule } from './subscription/subscription.module';
import { BcadRedemptionModule } from './redemption/redemption.module';
import { BcadViewPaymentModule } from './viewpayment/viewpayment.module';
import { BcadReportdeletionModule } from './reportdeletion/reportdeletion.module';
import { JhiMainComponent, NavbarComponent, FooterComponent, ErrorComponent } from './layouts';
import { AppComponent } from './main/webapp/app/app.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { OrderModule } from 'ngx-order-pipe';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgSelectModule } from '@ng-select/ng-select';
import { ClientCommissionModule } from './client-commissions/client-commission.module';

@NgModule({
    imports: [
        BrowserModule,
        BcadAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        BcadSharedModule,
        BcadCoreModule,
        BcadHomeModule,
        BcadAccountModule,
        BcadEntityModule,
        BcadLocationModule,
        BcadSubRMModule,
        BcadFileuploadModule,
        BcadTrailUpfrontPayModule,
        BcadReportGenerationModule,
        // BcadfileUploadModule,
        BcadClientmasterModule,
        BcadClientFeeCommissionModule,
        BcadProductModule,
        BcadDistributionOptionModule,
        BcadDistributionTypeModule,
        BcadDistributorMasterModule,
        BcadInvestmentModule,
        BcadRmModule,
        BcadRoleModule,
        BcadSeriesModule,
        BcadCdModule,
        BcadBulkUploadModule,
        // BcadAif2SeriesMasterModule,
        BcadAIF2ManagementFeeModule,
        BcadSubscriptionModule,
        BcadRedemptionModule,
        BcadViewPaymentModule,
        BcadReportdeletionModule,
        NgxPaginationModule,
        OrderModule,
        FormsModule,
        NgMultiSelectDropDownModule.forRoot(),
        ClientCommissionModule,
        NgSelectModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, FooterComponent, AppComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [StateStorageService, Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class BcadAppModule {}
