import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AIF2Fee } from './aif2-management-fee.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class AIF2FeeService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/aif2-management', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/aif2-management/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/aif2-management', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/aif2-management' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<AIF2Fee[]>> {
        const options = createRequestOption(req);
        return this.http.get<AIF2Fee[]>(SERVER_API_URL + 'api/aif2-managements', { params: options, observe: 'response' });
    }

    // get() {
    //     return this.http.get<aif2-management-fee[]>(SERVER_API_URL + 'api/users/aif2-management-fee');
    // }

    // searchaif2-management-fee(_data) {
    //     return this.http.post<aif2-management-fee[]>(SERVER_API_URL + 'api/aif2-management-fee-search', _data);
    // }
}
