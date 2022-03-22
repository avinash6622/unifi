import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class locationmanagement {
    public editdata: any = [];

    // public params = new HttpParams().set('clientname', this.clientservice.clientname).set('clientcode', this.clientservice.clientcode).set('':'');
    constructor(private http: HttpClient) {}

    save(location): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/location', location);
    }

    search(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/location');
    }

    findLocation(locationmanagement: any): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/location/{id}', locationmanagement);
    }
    findByLocationcode(locationmanagement: any): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/location/{locationcode}', locationmanagement);
    }
    deleteLocation(id): Observable<any> {
        return this.http.delete(SERVER_API_URL + 'api/location/{id}');
    }
    updateLocation(id): Observable<any> {
        return this.http.put(SERVER_API_URL + 'api/location/{id}', location);
    }
}
