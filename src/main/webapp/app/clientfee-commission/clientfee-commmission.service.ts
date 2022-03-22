import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClientFeeCommission } from './clientfee-commission.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ClientFeeCommissionService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/client-fee', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/client-fee/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/client-fee', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/client-fee' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<ClientFeeCommission[]>> {
        const options = createRequestOption(req);
        return this.http.get<ClientFeeCommission[]>(SERVER_API_URL + 'api/client-fees', { params: options, observe: 'response' });
    }

    pms() {
        return this.http.get(SERVER_API_URL + 'api/pms-clients-all');
    }

    clientFeeSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/clientFee-search-all', _data);
    }
}
