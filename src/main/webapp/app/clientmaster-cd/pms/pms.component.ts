import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import swal from 'sweetalert2';
import { PmsService } from './pms.service';
import { Pms } from './pms.model';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { DistributorMasterService } from '../../distributor-master/distributor-master.service';
import { RmService } from '../../rm/rm.service';

@Component({
    selector: 'jhi-pms',
    templateUrl: './pms.component.html',
    styleUrls: ['./pms.css']
})
export class PmsComponent implements OnInit {
    pms: Pms[] = [];
    account: Account;
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'clientCode';
    reverse: boolean = false;
    selectedItem: any;
    searchDataService: any;
    searchData: boolean;
    pmsClients: any;
    allClients: any;
    allDistributors: any;
    allRms: any;
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

    constructor(
        private router: Router,
        private pmsService: PmsService,
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
        this.listItesmsPerPage = 100;
        this.pmsList(this.pageNumber);
        this.pmsService.pms().subscribe(pmsClients => {
            this.pmsClients = pmsClients;
            console.log(this.pmsClients, 'pmsClients');
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
        this.pmsService.pmsSearch(this.allSearch).subscribe(allDatas => {
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
        this.pmsList(this.pageNumber);
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

    pmsList(pageNumber?: number) {
        this.dataSearch = false;
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.pmsService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe((res: HttpResponse<Pms[]>) => this.success(res.body, res.headers), (res: HttpResponse<any>) => this.error(res.body));
    }

    success(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.pms = data;
    }

    error(data) {
        console.log(data);
    }

    deletePm(pm): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.pmsService.delete(pm.id).subscribe(data => {
                    this.pms = this.pms.filter(u => u !== pm);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.pmsList();
    }
}
