import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DistType } from './distribution-type.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class DistributionTypeService {
    constructor(private http: HttpClient) {}

    add(type) {
        return this.http.post(SERVER_API_URL + '/api/distributor-type/', type);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + '/api/distributor-type/' + id);
    }

    update(distTypeName) {
        console.log('postdata', distTypeName);
        return this.http.put(SERVER_API_URL + '/api/distributor-type/', distTypeName);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/distributor-type/' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<DistType[]>> {
        const distributiontypes = createRequestOption(req);
        return this.http.get<DistType[]>(SERVER_API_URL + '/api/distributor-types/', { params: distributiontypes, observe: 'response' });
    }

    distributorType(): Observable<DistType[]> {
        return this.http.get<DistType[]>(SERVER_API_URL + 'api/distributor-types');
    }
}
