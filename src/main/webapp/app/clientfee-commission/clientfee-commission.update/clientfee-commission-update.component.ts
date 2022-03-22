import { ClientFeeCommission } from './../clientfee-commission.model';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ClientFeeCommissionService } from './../clientfee-commmission.service';
import swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
    selector: 'jhi-clientfee-commission-update',
    templateUrl: './clientfee-commission-update.component.html'
})
export class ClientFeeCommissionUpdateComponent implements OnInit {
    clientfee: ClientFeeCommission;
    links: any;
    id: number;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private clientfeecommissionservice: ClientFeeCommissionService,
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
        this.clientfeecommissionservice.find(id).subscribe(clientfeecommission => {
            this.clientfee = clientfeecommission;
        });
    }

    onSubmit() {
        this.clientfeecommissionservice.update(this.clientfee).subscribe(data => {
            if (data) {
                this.router.navigate(['/clientfee-Commission']);
                swal('ClientFeeCommission Updated Successfully');
            } else {
            }
        });
    }
}
