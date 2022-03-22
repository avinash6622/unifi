import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DistributorMasterService } from './../distributor-master.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { DistributorMaster } from '../distributor-master.model';
import swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';
import { Location } from '@angular/common';
import { Principal } from 'app/core';
import { Account } from './../../core/user/account.model';
import { TypeaheadMatch } from 'ngx-bootstrap/typeahead/typeahead-match.class';

@Component({
    selector: 'jhi-distributor-master',
    templateUrl: './distributor-master.component.html'
})
export class DistributorMasterComponent implements OnInit {
    account: Account;
    distributors: DistributorMaster[] = [];
    links: any;
    listPage: number;
    listItesmsPerPage: number;
    listTotalItems: number;
    pageNumber: number;
    sortKey: any;
    order: string = 'distName';
    reverse: boolean = false;
    distributorMaster: any;
    _distributorMaster: any;
    PaginationFlag: boolean = true;
    dropdownList = [];
    selectedItems = [];
    dropdownSettings = {};
    closeDropdownSelection: boolean = false;
    _distributorsSearch: any = [];
    findScreenData: any = [];
    DistName: any;
    searchData: boolean;
    searchDataService: any;
    selectedItem: any;
    searchItems: Array<Number> = [];
    searchNumber: any;
    options = [{ id: 1, optionName: 'Trail' }, { id: 2, optionName: 'Upfront' }];
    allClients: any;
    allTypes: any;
    allDistributors: any;
    allRms: any;
    distModelTypes: Array<String> = [];
    name: Array<String> = [];
    distType: Array<Number> = [];
    rmId: Array<Number> = [];
    allSearch: any;
    allDatas: any;
    ClientCode: any;
    clientName: any;
    DistributorName: any;
    RmName: any;
    dataSearch: boolean;
    distModelType: any;

    constructor(
        private router: Router,
        private distributionMasterService: DistributorMasterService,
        private parseLinks: JhiParseLinks,
        private _location: Location,
        private principal: Principal
    ) {
        this.sortKey = '';
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.listPage = 0;
        this.listItesmsPerPage = 100;
        this.distributiorMasterList(this.pageNumber);
        this.distributorMaster = {};
        this._distributorMaster = {
            distName: ''
        };
        this.findByDistName();
        console.log('_distributorsSearch', this._distributorsSearch);
        this.allDistributors = [];
        this.distributionMasterService.allDistributors().subscribe(allDistributors => {
            this.allDistributors = allDistributors;
            console.log(this.allDistributors, 'allDistributors');
        });
        // this.options = [];
        // this.distributionMasterService.distOptions().subscribe(options => {
        //     this.options = options;
        //     console.log(this.options, 'options');
        // });
        this.allRms = [];
        this.distributionMasterService.getRM().subscribe(allRms => {
            this.allRms = allRms;
            console.log(this.allRms, 'allRms');
        });
        this.allTypes = [];
        this.distributionMasterService.getType().subscribe(allTypes => {
            this.allTypes = allTypes;
            console.log(this.allTypes, 'allTypes');
        });
    }

    search() {
        this.allSearch = { distModelType: this.distModelTypes, distType: this.distType, clientNames: this.name, rmId: this.rmId };
        console.log(this.allSearch, '123');
        this.distributionMasterService.distributorSearch(this.allSearch).subscribe(allDatas => {
            this.allDatas = allDatas;
            console.log(this.allDatas, 'allDatas');
        });
        this.dataSearch = true;
    }

    clear() {
        this.distType = [];
        this.name = [];
        this.distModelTypes = [];
        this.rmId = [];
        this.distType = [];
        this.distModelType = [];
        this.clientName = [];
        this.RmName = [];
        this.distributiorMasterList(this.pageNumber);
    }

    onItemSelect(item: any) {
        this.selectedItem = item.id;
        this.distType.push(this.selectedItem);
        console.log(this.distType);
    }

    onItemSelect1(item: any) {
        this.selectedItem = item.optionName;
        this.distModelTypes.push(this.selectedItem);
        console.log(this.distModelTypes);
    }

    onItemSelect2(item: any) {
        this.selectedItem = item.distName;
        this.name.push(this.selectedItem);
        console.log(this.name);
    }

    onItemSelect3(item: any) {
        this.selectedItem = item.id;
        this.rmId.push(this.selectedItem);
        console.log(this.rmId);
    }

    onDeSelect(event) {
        console.log(event);
        console.log(event);
        const index: number = this.distType.indexOf(event.value.id);
        if (index !== -1) {
            this.distType.splice(index, 1);
        }
        console.log(this.distType);
    }

    onDeSelect1(event) {
        console.log(event);
        console.log(event);
        const index: number = this.distModelTypes.indexOf(event.value.optionName);
        if (index !== -1) {
            this.distModelTypes.splice(index, 1);
        }
        console.log(this.distModelTypes);
    }

    onDeSelect2(event) {
        console.log(event);
        const index: number = this.name.indexOf(event.value.distName);
        if (index !== -1) {
            this.name.splice(index, 1);
        }
        console.log(this.name);
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

    backClicked() {
        this._location.back();
    }

    distributiorMasterList(pageNumber?: number) {
        this.dataSearch = false;
        if (pageNumber) {
            this.listPage = pageNumber;
        }
        this.PaginationFlag = true;
        this.distributors = [];

        this.distributionMasterService
            .list({
                page: this.listPage - 1,
                size: this.listItesmsPerPage,
                sort: [this.sortKey]
            })
            .subscribe(
                (res: HttpResponse<DistributorMaster[]>) => {
                    this.success(res.body, res.headers);
                    console.log('rererer', res.body);
                },
                (res: HttpResponse<any>) => this.error(res.body)
            );
    }

    findByDistName() {
        this.distributionMasterService.get().subscribe(data => {
            this.findScreenData = data;
        });
    }

    success(data, headers) {
        console.log('data', data);
        this._distributorsSearch = data;
        this.links = this.parseLinks.parse(headers.get('link'));
        this.listTotalItems = headers.get('x-total-count');
        this.distributors = data;

        console.log('object', this._distributorsSearch);
    }

    success_1(data) {
        console.log('data12121212', data);
        this.distributors = data;
    }

    error(data) {
        console.log(data);
    }

    deleteDistributorMaster(distributor): void {
        swal({
            title: 'Are you sure?',
            text: 'Record will be removed',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.value) {
                this.distributionMasterService.delete(distributor.id).subscribe(data => {
                    this.distributors = this.distributors.filter(u => u !== distributor);
                });
                swal('Deleted!', 'Your file has been deleted.', 'success');
            }
        });
    }

    setOrder(value: string) {
        console.log(value);
        this.reverse = !this.reverse;
        this.sortKey = value + ',' + (this.reverse ? 'asc' : 'desc');
        this.distributiorMasterList();
    }
}
