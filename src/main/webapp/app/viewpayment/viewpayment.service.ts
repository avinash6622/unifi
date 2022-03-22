import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ViewPayment } from './viewpayment.model';

@Injectable()
export class ViewPaymentService {
    constructor(private http: HttpClient) {}

    save(payment) {
        return this.http.post(SERVER_API_URL + '/api/download-payment/', payment);
    }
}
