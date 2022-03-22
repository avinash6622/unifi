import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IProduct } from './product.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ProductService {
    constructor(private http: HttpClient) {}

    add(product) {
        return this.http.post(SERVER_API_URL + 'api/product', product);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/product/' + id);
    }

    update(product) {
        console.log('postdata', product);
        return this.http.put(SERVER_API_URL + 'api/product', product);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/product' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<IProduct[]>> {
        const options = createRequestOption(req);
        return this.http.get<IProduct[]>(SERVER_API_URL + 'api/products', { params: options, observe: 'response' });
    }

    product(): Observable<IProduct[]> {
        return this.http.get<IProduct[]>(SERVER_API_URL + 'api/produc');
    }

    aif2product(): Observable<IProduct[]> {
        return this.http.get<IProduct[]>(SERVER_API_URL + 'api/aif2products');
    }
}
