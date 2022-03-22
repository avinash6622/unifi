import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pms } from './pms.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class PmsService {
    constructor(private http: HttpClient) {}

    add(pms) {
        return this.http.post(SERVER_API_URL + 'api/pms-client', pms);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/pms-client/' + id);
    }

    update(pms) {
        console.log('postdata', pms);
        return this.http.put(SERVER_API_URL + 'api/pms-client', pms);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/pms-client' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Pms[]>> {
        const clients = createRequestOption(req);
        return this.http.get<Pms[]>(SERVER_API_URL + 'api/pms-clients', { params: clients, observe: 'response' });
    }

    getClientCodeSearchService(_data) {
        return this.http.post(SERVER_API_URL + 'api/PMS-search', _data);
    }

    pms(): Observable<Pms[]> {
        return this.http.get<Pms[]>(SERVER_API_URL + 'api/pms-clients-all');
    }

    pmsSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/PMS-search-all', _data);
    }
}
