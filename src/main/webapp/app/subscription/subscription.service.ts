import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Subscription } from 'app/subscription/subscription.model';

@Injectable()
export class SubscriptionService {
    constructor(private http: HttpClient) {}

    save(clients) {
        return this.http.post(SERVER_API_URL + 'api/aif-subscription', clients);
    }
}
