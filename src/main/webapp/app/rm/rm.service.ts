import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Rm } from './rm.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class RmService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/rm', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/rm/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/rm', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/rm' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Rm[]>> {
        const options = createRequestOption(req);
        return this.http.get<Rm[]>(SERVER_API_URL + 'api/rms', { params: options, observe: 'response' });
    }

    get() {
        return this.http.get<Rm[]>(SERVER_API_URL + 'api/users/rm');
    }

    searchRm(_data) {
        return this.http.post<Rm[]>(SERVER_API_URL + 'api/rm-search', _data);
    }
}
