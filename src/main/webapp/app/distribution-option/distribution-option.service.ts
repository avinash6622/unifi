import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Distribution } from './distribution-option.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class DistributionService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/option', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/option/' + id);
    }

    // find(id: number): Observable<HttpResponse<Location>> {
    //     return this.http.get<Location>(SERVER_API_URL + 'api/location/' + id, { observe: 'response' });
    // }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/option', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/option' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Distribution[]>> {
        const options = createRequestOption(req);
        return this.http.get<Distribution[]>(SERVER_API_URL + 'api/options', { params: options, observe: 'response' });
    }
    distributorOptions(): Observable<Distribution[]> {
        return this.http.get<Distribution[]>(SERVER_API_URL + 'api/options');
    }
}
