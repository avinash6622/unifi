import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Investment } from './investment.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class InvestmentService {
    constructor(private http: HttpClient) {}

    add(investment) {
        return this.http.post(SERVER_API_URL + 'api/invest-master', investment);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/invest-master/' + id);
    }

    // find(id: number): Observable<HttpResponse<Location>> {
    //     return this.http.get<Location>(SERVER_API_URL + 'api/location/' + id, { observe: 'response' });
    // }

    update(investment) {
        console.log('postdata', investment);
        return this.http.put(SERVER_API_URL + 'api/invest-master', investment);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/invest-master' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Investment[]>> {
        const options = createRequestOption(req);
        return this.http.get<Investment[]>(SERVER_API_URL + 'api/invest-masters', { params: options, observe: 'response' });
    }

    investment(): Observable<Investment[]> {
        return this.http.get<Investment[]>(SERVER_API_URL + 'api/invest-masters');
    }
}
