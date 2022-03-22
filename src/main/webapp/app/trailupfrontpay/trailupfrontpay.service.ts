import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class TrailUpfrontPayService {
    constructor(private http: HttpClient) {}

    save(TrailUpfrontPay) {
        return this.http.post(SERVER_API_URL + 'api/trailupfrontpay', TrailUpfrontPay);
    }

    afterSave(TrailUpfrontPay) {
        return this.http.post(SERVER_API_URL + 'api/trailupfrontpay-repeat', TrailUpfrontPay);
    }
}
