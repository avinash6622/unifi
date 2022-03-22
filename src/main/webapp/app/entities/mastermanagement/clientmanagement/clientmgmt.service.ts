import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { client } from 'webstomp-client';

@Injectable({ providedIn: 'root' })
export class ClientMgmt {
    constructor(private http: HttpClient) {}

    save(client): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/client', client);
    }

    search(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/client');
    }

    findClient(clientManagment: any): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/client/{id}', clientManagment);
    }
    findByClientcode(clientManagment: any): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/client/{clientcode}', clientManagment);
    }
    deleteClient(id): Observable<any> {
        return this.http.delete(SERVER_API_URL + 'api/client/{id}');
    }
    updateClient(id): Observable<any> {
        return this.http.put(SERVER_API_URL + 'api/client/{id}', client);
    }
}
