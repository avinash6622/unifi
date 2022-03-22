import { HasAnyAuthorityDirective } from './../../shared/auth/has-any-authority.directive';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ClientmasterService } from './../clientmaster-cd.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Client } from '../clientmaster-cd.model';
import swal from 'sweetalert2';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead/typeahead-match.class';
import { Account } from './../../core/user/account.model';
import { Principal } from 'app/core';
import { DistributorMasterService } from '../../distributor-master/distributor-master.service';
import { RmService } from '../../rm/rm.service';

@Component({
    selector: 'jhi-clientmaster-cd',
    templateUrl: './clientmaster-cd.component.html',
    styleUrls: ['./clientmaster.css']
})
export class ClientmasterComponent implements OnInit {
    modalRef: NgbModalRef;
    account: Account;
    clients: Client[];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'clientCode';
    reverse: boolean = false;
    ClientCode: any;
    ClientName: any;
    DistributorName: any;
    RmName: any;
    selectedItem: any;
    searchDataService: any;
    searchData: boolean;
    allDatas: any;
    allClients: any;
    allDistributors: any;
    allRms: any;
    clientCodes: Array<Number> = [];
    clientNames: Array<String> = [];
    distMasterId: Array<Number> = [];
    rmId: Array<Number> = [];
    allSearch: any;
    dataSearch: boolean;
    selectedCityIds: any;

    constructor(
        private router: Router,
        private dialog: NgbModal,
        private clientmasterService: ClientmasterService,
        private parseLinks: JhiParseLinks,
        private principal: Principal,
        private distributionMasterService: DistributorMasterService,
        private rmService: RmService
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 50;
        this.clientmasterList(this.pageNumber);
        this.clients = [];
        this.searchDataService = {};
        this.clientmasterService.allClients().subscribe(allClients => {
            this.allClients = allClients;
            console.log(this.allClients, 'allClients');
        });
        this.allDistributors = [];
        this.distributionMasterService.allDistributors().subscribe(allDistributors => {
            this.allDistributors = allDistributors;
            console.log(this.allDistributors, 'allDistributors');
        });
        this.allRms = [];
        this.rmService.get().subscribe(allRms => {
            this.allRms = allRms;
            console.log(this.allRms, 'allRms');
        });
    }

    search() {
        this.allSearch = { clientCodes: this.clientCodes, clientNames: this.clientNames, distMasterId: this.distMasterId, rmId: this.rmId };
        console.log(this.allSearch, '123');
        this.clientmasterService.allSearch(this.allSearch).subscribe(allDatas => {
            this.allDatas = allDatas;
            console.log(this.allDatas, 'allDatas');
        });
        this.dataSearch = true;
    }

    clear() {
        this.clientCodes = [];
        this.clientNames = [];
        this.distMasterId = [];
        this.rmId = [];
        this.ClientCode = [];
        this.ClientName = [];
        this.DistributorName = [];
        this.RmName = [];
        this.clientmasterList(this.pageNumber);
    }

    onItemSelect(item: any) {
        this.selectedItem = item.clientCode;
        this.clientCodes.push(this.selectedItem);
        console.log(this.clientCodes);
    }

    onItemSelect1(item: any) {
        this.selectedItem = item.clientName;
        this.clientNames.push(this.selectedItem);
        console.log(this.clientNames);
    }

    onItemSelect2(item: any) {
        this.selectedItem = item.id;
        this.distMasterId.push(this.selectedItem);
        console.log(this.distMasterId);
    }

    onItemSelect3(item: any) {
        this.selectedItem = item.id;
        this.rmId.push(this.selectedItem);
        console.log(this.rmId);
    }

    onDeSelect(event) {
        console.log(event);
        console.log(event);
        const index: number = this.clientCodes.indexOf(event.value.clientCode);
        if (index !== -1) {
            this.clientCodes.splice(index, 1);
        }
        console.log(this.clientCodes);
    }

    onDeSelect1(event) {
        console.log(event);
        console.log(event);
        const index: number = this.clientNames.indexOf(event.value.clientName);
        if (index !== -1) {
            this.clientNames.splice(index, 1);
        }
        console.log(this.clientNames);
    }

    onDeSelect2(event) {
        console.log(event);
        const index: number = this.distMasterId.indexOf(event.value.id);
        if (index !== -1) {
            this.distMasterId.splice(index, 1);
        }
    }

    onDeSelect3(event) {
        console.log(event);
        const index: number = this.rmId.indexOf(event.value.id);
        if (index !== -1) {
            this.rmId.splice(index, 1);
        }
    }

    success1(data) {
        this.searchDataService = data;
        console.log('data', this.searchDataService);
    }

    clientmasterList(pageNumber?: number) {
        this.dataSearch = false;
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.clientmasterService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<Client[]>) => this.success(res.body, res.headers),
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    success(data, headers) {
        this.searchData = false;
        this.listTotalItems = headers.get('x-total-count');
        this.clients = data;
    }

    error(data) {
        console.log(data);
    }

    deleteClientMaster(client): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.clientmasterService.delete(client.id).subscribe(data => {
                    this.clients = this.clients.filter(u => u !== client);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.clientmasterList();
    }
}
