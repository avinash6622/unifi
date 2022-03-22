import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ReportGenerationService {
    constructor(private http: HttpClient) {}

    save(reportGeneration) {
        return this.http.post(SERVER_API_URL + '/api/report-generates/', reportGeneration);
    }
}
