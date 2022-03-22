import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Aif2series } from './aif2-series-master.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class AIF2Service {
    constructor(private http: HttpClient) {}

    add(aif) {
        return this.http.post(SERVER_API_URL + 'api/aif2-series', aif);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/aif2-series/' + id);
    }

    update(aif) {
        console.log('postdata', aif);
        return this.http.put(SERVER_API_URL + 'api/aif2-series', aif);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/aif2-series' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Aif2series[]>> {
        const options = createRequestOption(req);
        return this.http.get<Aif2series[]>(SERVER_API_URL + 'api/aif2-seriess', { params: options, observe: 'response' });
    }

    aif2Series(): Observable<Aif2series[]> {
        return this.http.get<Aif2series[]>(SERVER_API_URL + 'api/aif2-seriess');
    }

    aif2SeriesandBlend(id: number) {
        return this.http.get(SERVER_API_URL + 'api/aif2-series-product/' + id);
    }
}
