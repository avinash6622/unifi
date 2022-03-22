import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Cd } from './cd.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class CdService {
    constructor(private http: HttpClient) {}

    add(commission) {
        return this.http.post(SERVER_API_URL + 'api/commission-def', commission);
    }

    list(req?: any): Observable<HttpResponse<Cd[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cd[]>(SERVER_API_URL + 'api/commission-defs', { params: options, observe: 'response' });
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/commission-def' + '/' + id);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/commission-def/' + id);
    }

    update(commission) {
        console.log('postdata', commission);
        return this.http.put(SERVER_API_URL + 'api/commission-def', commission);
    }

    dropdownChange(id) {
        return this.http.get(SERVER_API_URL + 'api/client-product' + '/' + id);
    }

    productChange(id) {
        return this.http.get(SERVER_API_URL + 'api/commission-def/product' + '/' + id);
    }
    pmsChange(id, pmsInvest) {
        console.log('entering');
        return this.http.get(SERVER_API_URL + 'api/commission-def/product' + '/' + id + '/' + pmsInvest);
    }

    optionProductChange(id, pmsInvest) {
        return this.http.get(SERVER_API_URL + 'api/commission-def/option3' + '/' + id + '/' + pmsInvest);
    }
}
