import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Subscription } from 'app/subscription/subscription.model';

@Injectable()
export class RedemptionService {
    constructor(private http: HttpClient) {}

    save(clients) {
        return this.http.post(SERVER_API_URL + 'api/redemptions', clients);
    }

    seriesValue(clients: any, redemptionDate: any) {
        return this.http.post(SERVER_API_URL + 'api/change-series' + '?redemptionDate=' + redemptionDate, clients);
    }

    find(id: number) {
        return this.http.get(SERVER_API_URL + 'api/redemption-clients/' + id);
    }

    getValues(clients: any) {
        return this.http.post(SERVER_API_URL + 'api/redemption-units', clients);
    }
}
