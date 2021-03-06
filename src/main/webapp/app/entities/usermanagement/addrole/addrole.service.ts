import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AddRole {
    public editdata: any = [];

    constructor(private http: HttpClient) {}

    save(role): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/role', role);
    }

    search(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/role');
    }
}
