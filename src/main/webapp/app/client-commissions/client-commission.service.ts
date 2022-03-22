import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClientCommission } from './client-commission.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ClientCommissionService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/client-comm', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/client-comm/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/client-comm', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/client-comm' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<ClientCommission[]>> {
        const options = createRequestOption(req);
        return this.http.get<ClientCommission[]>(SERVER_API_URL + 'api/client-comm', { params: options, observe: 'response' });
    }

    pms() {
        return this.http.get(SERVER_API_URL + 'api/clients-all');
    }

    clientFeeSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/clientComm-search-all', _data);
    }
}
