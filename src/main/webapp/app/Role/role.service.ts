import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { RoleModel } from './role-add/role-master.model';

@Injectable()
export class RoleService {
    constructor(private http: HttpClient) {}

    add(option) {
        return this.http.post(SERVER_API_URL + 'api/role', option);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/role/' + id);
    }

    update(option) {
        console.log('postdata', option);
        return this.http.put(SERVER_API_URL + 'api/role', option);
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/role' + '/' + id);
    }

    list(req?: any): Observable<HttpResponse<RoleModel[]>> {
        const options = createRequestOption(req);
        return this.http.get<RoleModel[]>(SERVER_API_URL + 'api/roles', { params: options, observe: 'response' });
    }
}
