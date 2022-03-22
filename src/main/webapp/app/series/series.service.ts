import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Series } from './series.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class SeriesService {
    constructor(private http: HttpClient) {}

    add(series) {
        return this.http.post(SERVER_API_URL + 'api/series-master', series);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/series-master/' + id);
    }

    update(series) {
        console.log('postdata', series);
        return this.http.put(SERVER_API_URL + 'api/series-master', series);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/series-master' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Series[]>> {
        const series = createRequestOption(req);
        return this.http.get<Series[]>(SERVER_API_URL + 'api/series-masters', { params: series, observe: 'response' });
    }

    series(): Observable<Series[]> {
        return this.http.get<Series[]>(SERVER_API_URL + 'api/series-master-all');
    }
}
