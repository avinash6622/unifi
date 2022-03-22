import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUser } from './user.model';
import { IDistributor } from 'app/entities/mastermanagement/distmanagement/distmanagement.model';
import { IRM } from 'app/entities/mastermanagement/rmmanagement/rmmanagement.model';
import { ISubRM } from 'app/entities/mastermanagement/subrmmanagement/subrmmanagement.model';
import { IRole } from 'app/Role/Role.model';

@Injectable({ providedIn: 'root' })
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';

    constructor(private http: HttpClient) {}

    create(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.post<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    update(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IUser>> {
        return this.http.get<IUser>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }
    distributors(): Observable<IDistributor[]> {
        return this.http.get<IDistributor[]>(SERVER_API_URL + 'api/users/dist-master');
    }
    distributorsLogin(): Observable<IDistributor[]> {
        return this.http.get<IDistributor[]>(SERVER_API_URL + 'api/report/dist-masters');
    }
    distributorsViewPayment(): Observable<IDistributor[]> {
        return this.http.get<IDistributor[]>(SERVER_API_URL + 'api/payment/dist-masters');
    }
    distributorsfilter(): Observable<IDistributor[]> {
        return this.http.get<IDistributor[]>(SERVER_API_URL + 'api/commission-def/dist-master');
    }
    relations(): Observable<IRM[]> {
        return this.http.get<IRM[]>(SERVER_API_URL + 'api/users/rm');
    }
    subRMs(): Observable<ISubRM[]> {
        return this.http.get<ISubRM[]>(SERVER_API_URL + 'api/users/sub-rm');
    }
    roles(): Observable<IRole[]> {
        return this.http.get<IRole[]>(SERVER_API_URL + 'api/role-all');
    }
}
