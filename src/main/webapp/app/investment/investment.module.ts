import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';

import { investmentState } from './';

import { InvestmentComponent } from './investment/investment.component';
import { InvestmentAddComponent } from './investment-add/investment-add.component';
import { InvestmentUpdateComponent } from './investment-update/investment-update.component';
import { InvestmentService } from './investment.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [NgxPaginationModule, NgbModule, RouterModule.forChild(investmentState), FormsModule, CommonModule],
    declarations: [InvestmentAddComponent, InvestmentComponent, InvestmentUpdateComponent],
    entryComponents: [InvestmentAddComponent, InvestmentComponent, InvestmentUpdateComponent],
    exports: [InvestmentAddComponent, InvestmentUpdateComponent],
    providers: [InvestmentService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadInvestmentModule {}
