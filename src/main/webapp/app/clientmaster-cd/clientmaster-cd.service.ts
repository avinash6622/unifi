import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Client } from './clientmaster-cd.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ClientmasterService {
    constructor(private http: HttpClient) {}

    add(client) {
        return this.http.post(SERVER_API_URL + 'api/client', client);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/clients/' + id);
    }

    update(client) {
        console.log('postdata', client);
        return this.http.put(SERVER_API_URL + 'api/client', client);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/client' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Client[]>> {
        const clients = createRequestOption(req);
        return this.http.get<Client[]>(SERVER_API_URL + 'api/clients', { params: clients, observe: 'response' });
    }
    dropdownChange(id) {
        return this.http.get(SERVER_API_URL + 'api/client-product' + '/' + id);
    }

    getClientCode() {
        return this.http.get(SERVER_API_URL + 'api/client');
    }

    getClientCodeSearchService(_data) {
        return this.http.post(SERVER_API_URL + 'api/client-search', _data);
    }

    allClients(): Observable<Client[]> {
        return this.http.get<Client[]>(SERVER_API_URL + 'api/clients-all');
    }

    allSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/client-search-all', _data);
    }
}
