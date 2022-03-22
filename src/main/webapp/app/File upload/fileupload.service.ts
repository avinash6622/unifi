import { HttpClient, HttpResponse, HttpRequest, HttpEvent, HttpEventType, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Fileupload } from './fileupload.model';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { map, tap, last, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class FileuploadService {
    constructor(private http: HttpClient) {}

    add(data) {
        // return this.http.post(SERVER_API_URL + 'api/file-upload' + '/startdate=' + startDate +
        //         '&' + 'enddate=' + endDate + '&' + 'filename=' + fileName + '&' + 'fileupload=' + fileUpload );

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
        return this.http.post(SERVER_API_URL + 'api/file-upload', formData, {
            params: _UploadData,
            reportProgress: true
        });
        /* return this.http.post(SERVER_API_URL + 'api/file-upload', formData, {
            params: _UploadData,
            reportProgress: true
        }).pipe(
            map(event => this.getEventMessage(event, _inputFile)),
            tap(message => this.showProgress(message)),
            last(), // return last (completed) message to caller
            catchError(error => this.handleError(error))
        ); */
        // const req = new HttpRequest('POST', SERVER_API_URL + 'api/aif-file-upload', formData, {
        //     params: qParams,
        //     reportProgress: true
        // });
        // return this.http.request(req).pipe(
        //     map(event => this.getEventMessage(event, _inputFile)),
        //     tap(message => this.showProgress(message)),
        //     last(), // return last (completed) message to caller
        //     catchError(error => this.handleError(error))
        // );
    }

    private showProgress(message: string) {
        console.log('......', message, new Date().getSeconds());
    }

    private handleError(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error.message);
        } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong,
            console.error(`Backend returned code ${error.status}, ` + `body was: ${error.error}`);
        }
        // return an observable with a user-facing error message
        return throwError('Something bad happened; please try again later.');
    }

    private getEventMessage(event: HttpEvent<any>, file: File) {
        switch (event.type) {
            case HttpEventType.Sent:
                return `Uploading file "${file.name}" of size ${file.size}.`;

            case HttpEventType.UploadProgress:
                // Compute and show the % done:
                const percentDone = Math.round(100 * event.loaded / event.total);
                return `File "${file.name}" is ${percentDone}% uploaded.`;

            case HttpEventType.Response:
                return `File "${file.name}" was completely uploaded!`;

            default:
                return `File "${file.name}" surprising upload event: ${event.type}.`;
        }
    }

    aifUploadFile(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/aif-file-upload', formData, {
            params: _UploadData
        });
    }

    // /api/upload/transactionReport

    aifTransactionUploadFile(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/upload/transactionReport', formData, {
            params: _UploadData
        });
    }

    aifBlendTransactionUploadFile(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/upload/umbrella/managementFee', formData, {
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

    upfrontUpload(_UploadData, _inputFile) {
        console.log('_UploadData', _UploadData);
        const formData: FormData = new FormData();
        formData.append('fileUpload', _inputFile, _inputFile.name);
        console.log('formData', formData);
        return this.http.post(SERVER_API_URL + 'api/file-upload-upfront', formData, {
            params: _UploadData
        });
    }

    list(req?: any): Observable<HttpResponse<Fileupload[]>> {
        const fileuploads = createRequestOption(req);
        return this.http.get<Fileupload[]>(SERVER_API_URL + '/api/file-uploads', { params: fileuploads, observe: 'response' });
    }

    aiflist(req?: any): Observable<HttpResponse<Fileupload[]>> {
        const fileuploads = createRequestOption(req);
        return this.http.get<Fileupload[]>(SERVER_API_URL + '/api/aif-file-uploads', { params: fileuploads, observe: 'response' });
    }

    upfrontList(req?: any): Observable<HttpResponse<Fileupload[]>> {
        const fileuploads = createRequestOption(req);
        return this.http.get<Fileupload[]>(SERVER_API_URL + '/api/file-upload-upfronts', { params: fileuploads, observe: 'response' });
    }

    fileuploadmasterList(req?: any): Observable<HttpResponse<Fileupload[]>> {
        const fileuploads = createRequestOption(req);
        return this.http.get<Fileupload[]>(SERVER_API_URL + '/api/uploadmasters', { params: fileuploads, observe: 'response' });
    }

    delete(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/file-upload' + '/' + id);
    }

    deleteAif(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/aif-file-upload' + '/' + id);
    }

    deleteUpfront(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/file-upload-upfront' + '/' + id);
    }

    deletefileuploadMaster(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/uploadmaster' + '/' + id);
    }

    bcadDelete(id: string) {
        return this.http.delete(SERVER_API_URL + '/api/uploadmaster' + '/' + id);
    }

    bcadApprove(id: string) {
        return this.http.get(SERVER_API_URL + '/api/uploadmaster' + '/' + id);
    }

    pmsApprove(id: string) {
        return this.http.get(SERVER_API_URL + '/api/approve-file' + '/' + id);
    }

    aifApprove(id: string) {
        return this.http.get(SERVER_API_URL + '/api/approve-aif-file' + '/' + id);
    }

    upfrontApprove(id: string) {
        return this.http.get(SERVER_API_URL + '/api/approve-upfront-file' + '/' + id);
    }
}
