import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubRM } from './Sub-RM.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class SubRMService {
    constructor(private http: HttpClient) {}
    list(req?: any): Observable<HttpResponse<SubRM[]>> {
        const subrms = createRequestOption(req);
        return this.http.get<SubRM[]>(SERVER_API_URL + 'api/sub-rm', { params: subrms, observe: 'response' });
    }
    delete(id: string) {
        return this.http.delete(SERVER_API_URL + 'api/sub-rm' + '/' + id);
    }
    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/sub-rm/' + id);
    }
    add(subRM) {
        return this.http.post(SERVER_API_URL + 'api/sub-rm', subRM);
    }
    update(subRM) {
        console.log('postdata', subRM);
        return this.http.put(SERVER_API_URL + 'api/sub-rm', subRM);
    }

    get() {
        return this.http.get(SERVER_API_URL + 'api/users/sub-rm');
    }

    findBySubName(_data) {
        return this.http.post(SERVER_API_URL + 'api/sub-rm-search', _data);
    }
}
