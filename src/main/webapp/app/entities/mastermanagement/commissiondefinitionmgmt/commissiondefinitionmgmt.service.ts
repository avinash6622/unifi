// import { HttpClient } from '@angular/common/http';
// import { Injectable } from '@angular/core';
// import { Observable } from 'rxjs';
// import { SERVER_API_URL } from 'app/app.constants';

// @Injectable({ providedIn: 'root' })
// export class Commissiondefinitionmgmt {
//     constructor(private http: HttpClient) {}
// }
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class Commissiondefinitionmgmt {
    constructor(private http: HttpClient) {}
}
