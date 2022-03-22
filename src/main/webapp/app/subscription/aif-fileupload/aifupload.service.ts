import { HttpClient, HttpResponse, HttpRequest, HttpEvent, HttpEventType, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { map, tap, last, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class AifUploadService {
    constructor(private http: HttpClient) {}

    aifUploadFile(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/sub-file-upload', formData, {
            params: _UploadData
        });
    }

    fileuploadMaster(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/uploadmaster', formData, {
            params: _UploadData
        });
    }
}
