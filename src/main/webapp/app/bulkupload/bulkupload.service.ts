import { HttpClient, HttpResponse, HttpRequest, HttpEvent, HttpEventType, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BulkUpload } from './bulkupload.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable()
export class FileuploadService {
    constructor(private http: HttpClient) {}

    add(data) {
        return this.http.post(SERVER_API_URL + 'api/file-upload', {
            _params: data
        });
    }

    sendUploadFile(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const qParams = createRequestOption(_UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/pms-file-upload', formData, {
            params: _UploadData
        });
    }

    distupload(_UploadData, _inputFile) {
        console.log('bulk');
        const qParams = createRequestOption(_UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/bulkupload', formData, {
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
