import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ILocation } from './location.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class LocationService {
    constructor(private http: HttpClient) {}

    add(location) {
        return this.http.post(SERVER_API_URL + 'api/location', location);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/location/' + id);
    }

    // find(id: number): Observable<HttpResponse<Location>> {
    //     return this.http.get<Location>(SERVER_API_URL + 'api/location/' + id, { observe: 'response' });
    // }

    update(location) {
        console.log('postdata', location);
        return this.http.put(SERVER_API_URL + 'api/location', location);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/location' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<Location[]>> {
        const options = createRequestOption(req);
        return this.http.get<Location[]>(SERVER_API_URL + 'api/locations', { params: options, observe: 'response' });
    }

    location(): Observable<Location[]> {
        return this.http.get<Location[]>(SERVER_API_URL + 'api/locations');
    }
}
