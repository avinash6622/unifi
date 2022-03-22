import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ClientCommissionService } from './../client-commission.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';
import { ClientCommission } from './../client-commission.model';

@Component({
    selector: 'jhi-client-commission-update',
    templateUrl: './client-commission-update.component.html',
    styles: []
})
export class ClientCommissionUpdateComponent implements OnInit {
    clientfee: ClientCommission;
    id: number;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private clientfeecommissionservice: ClientCommissionService,
        private _location: Location
    ) {
        this.clientfee = {};
    }

    ngOnInit() {
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadClientfeecommission(this.id);
        });
    }

    cancel() {
        this._location.back();
    }

    loadClientfeecommission(id) {
        this.clientfeecommissionservice.find(id).subscribe(ClientCommission => {
            this.clientfee = ClientCommission;
        });
    }

    onSubmit() {
        this.clientfeecommissionservice.update(this.clientfee).subscribe(data => {
            if (data) {
                this.router.navigate(['/client-Commission']);
                swal('ClientCommission Updated Successfully');
            } else {
            }
        });
    }
}
