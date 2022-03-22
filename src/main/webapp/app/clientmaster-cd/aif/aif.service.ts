import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Aif } from './aif.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class AifService {
    constructor(private http: HttpClient) {}

    add(aif) {
        return this.http.post(SERVER_API_URL + 'api/aif-client', aif);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/aif-client/' + id);
    }

    update(aif) {
        console.log('postdata', aif);
        return this.http.put(SERVER_API_URL + 'api/aif-client', aif);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/aif-client' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Aif[]>> {
        const clients = createRequestOption(req);
        return this.http.get<Aif[]>(SERVER_API_URL + 'api/aif-clients', { params: clients, observe: 'response' });
    }

    aif(): Observable<Aif[]> {
        return this.http.get<Aif[]>(SERVER_API_URL + 'api/aif-clients-all');
    }

    getClientCodeSearchService(_data) {
        return this.http.post(SERVER_API_URL + 'api/AIF-search', _data);
    }

    aifSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/AIF-search-all', _data);
    }
}
