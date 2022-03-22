import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Reportdeletion } from './reportdeletion.model';

@Injectable()
export class ReportDeletionService {
    constructor(private http: HttpClient) {}

    save(report) {
        return this.http.post(SERVER_API_URL + '/api/report-deletion/', report);
    }
}
