import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DistributorMaster } from './distributor-master.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class DistributorMasterService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/distributor', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/distributor/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/distributor', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/distributor' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<DistributorMaster[]>> {
        const options = createRequestOption(req);
        return this.http.get<DistributorMaster[]>(SERVER_API_URL + 'api/distributors', { params: options, observe: 'response' });
    }

    get() {
        return this.http.get(SERVER_API_URL + 'api/users/dist-master');
    }

    filter(_data) {
        return this.http.post(SERVER_API_URL + 'api/dist-master-search', _data, {
            observe: 'response'
        });
    }

    allDistributors(): Observable<DistributorMaster[]> {
        return this.http.get<DistributorMaster[]>(SERVER_API_URL + 'api/distributors-all');
    }

    distributorSearch(_data) {
        return this.http.post(SERVER_API_URL + 'api/dist-search-all', _data);
    }

    distOptions() {
        return this.http.get(SERVER_API_URL + 'api/options');
    }

    getRM() {
        return this.http.get(SERVER_API_URL + 'api/users/rm');
    }

    getType() {
        return this.http.get(SERVER_API_URL + 'api/distributor-types');
    }
}
