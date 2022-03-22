import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { productState } from './';
import { ProductComponent } from './product/product.component';
import { ProductAddComponent } from './product-add/product-add.component';
import { ProductUpdateComponent } from './product-update/product-update.component';
import { ProductService } from './product.service';
import { NgxPaginationModule } from 'ngx-pagination';
import { OrderModule } from 'ngx-order-pipe';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    imports: [OrderModule, NgbModule, NgxPaginationModule, RouterModule.forChild(productState), FormsModule, CommonModule],
    declarations: [ProductAddComponent, ProductComponent, ProductUpdateComponent],
    entryComponents: [ProductComponent, ProductAddComponent, ProductUpdateComponent],
    exports: [ProductAddComponent, ProductUpdateComponent],
    providers: [ProductService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BcadProductModule {}
