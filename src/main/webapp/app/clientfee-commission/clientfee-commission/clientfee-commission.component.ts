import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientFeeCommissionService } from './../clientfee-commmission.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { ClientFeeCommission } from '../clientfee-commission.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { DistributorMasterService } from '../../distributor-master/distributor-master.service';

@Component({
    selector: 'jhi-clientfee-commission',
    templateUrl: './clientfee-commission.component.html'
})
export class ClientFeeCommissionComponent implements OnInit {
    clientfees: ClientFeeCommission[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'clientCode';
    reverse: boolean = false;
    pmsClients: any;
    allDistributors: any;
    clientCodes: Array<Number> = [];
    clientNames: Array<String> = [];
    distMasterId: Array<Number> = [];
    rmId: Array<Number> = [];
    allSearch: any;
    allDatas: any;
    ClientCode: any;
    ClientName: any;
    DistributorName: any;
    RmName: any;
    dataSearch: boolean;
    selectedItem: any;

    constructor(
        private router: Router,
        private clientfeecommissionService: ClientFeeCommissionService,
        private _location: Location,
        private parseLinks: JhiParseLinks,
        private distributionMasterService: DistributorMasterService
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.listPage = 0;
        this.listItesmsPerPage = 50;
        this.clientfeeCommissionList(this.pageNumber);
        this.clientfeecommissionService.pms().subscribe(pmsClients => {
            this.pmsClients = pmsClients;
            console.log(this.pmsClients, 'pmsClients');
        });
        this.allDistributors = [];
        this.distributionMasterService.allDistributors().subscribe(allDistributors => {
            this.allDistributors = allDistributors;
            console.log(this.allDistributors, 'allDistributors');
        });
    }

    search() {
        this.allSearch = { clientCodes: this.clientCodes, distMasterId: this.distMasterId };
        console.log(this.allSearch, '123');
        this.clientfeecommissionService.clientFeeSearch(this.allSearch).subscribe(allDatas => {
            this.allDatas = allDatas;
            console.log(this.allDatas, 'allDatas');
        });
        this.dataSearch = true;
    }

    clear() {
        this.clientCodes = [];
        this.distMasterId = [];
        this.ClientCode = [];
        this.DistributorName = [];
        this.clientfeeCommissionList(this.pageNumber);
    }

    onItemSelect(item: any) {
        console.log(item, '123');
        this.selectedItem = item.id;
        this.clientCodes.push(this.selectedItem);
        console.log(this.clientCodes);
    }

    onItemSelect2(item: any) {
        this.selectedItem = item.id;
        this.distMasterId.push(this.selectedItem);
        console.log(this.distMasterId);
    }

    onDeSelect(event) {
        console.log(event);
        console.log(event);
        const index: number = this.clientCodes.indexOf(event.value.id);
        if (index !== -1) {
            this.clientCodes.splice(index, 1);
        }
        console.log(this.clientCodes);
    }

    onDeSelect2(event) {
        console.log(event);
        const index: number = this.distMasterId.indexOf(event.value.id);
        if (index !== -1) {
            this.distMasterId.splice(index, 1);
        }
    }

    clientfeeCommissionList(pageNumber?: number) {
        this.dataSearch = false;
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.clientfeecommissionService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<ClientFeeCommission[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.clientfees = data;
    }

    error(data) {
        console.log(data);
    }

    deleteClientFeeCommisson(clientfees): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.clientfeecommissionService.delete(clientfees.id).subscribe(data => {
                    this.clientfees = this.clientfees.filter(u => u !== clientfees);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.clientfeeCommissionList();
    }
}
