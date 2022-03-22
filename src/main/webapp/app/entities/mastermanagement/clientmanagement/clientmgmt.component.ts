import { Component, OnInit } from '@angular/core';
import { ClientMgmt } from './clientmgmt.service';
import { workers } from 'cluster';
import { CompileShallowModuleMetadata } from '@angular/compiler';
@Component({
    selector: 'jhi-app-clientmgmt',
    templateUrl: './clientmgmt.component.html',
    styleUrls: ['./clientmgmt.component.css']
})
export class ClientmgmtComponent implements OnInit {
    isSearchFound: boolean = false;
    fetchData: boolean = false;
    isEdit: boolean = false;
    clientmanagement: any[];
    clientDataByCode: any[];
    updatedClient: any[];
    showByClientCode: boolean = false;

    client = {
        id: null,
        clientcode: '',
        clientname: '',
        strategy: '',
        rm: '',
        distributor: ''
    };

    showSaveAlerts: boolean = false;
    dontShowAlerts: boolean = false;
    showUpdateAlerts: boolean = false;

    constructor(private clientservice: ClientMgmt) {}

    public isDisplayclient: Boolean = false;
    public isAddclient: false;
    public isUpdateclient: false;

    ngOnInit() {}

    addClient() {
        this.isDisplayclient = true;
    }

    addclient(e) {
        this.isUpdateclient = false;
        this.isAddclient = e.target.checked;
        this.showSaveAlerts = false;
        this.dontShowAlerts = false;
        this.showUpdateAlerts = false;
    }

    updateclient(e) {
        this.isAddclient = false;
        this.isUpdateclient = e.target.checked;
        this.fetchData = false;
        this.showSaveAlerts = false;
        this.dontShowAlerts = false;
        this.showUpdateAlerts = false;
    }

    save(isEdit) {
        if (isEdit) {
            this.clientservice.updateClient(this.client.id).subscribe(updatedClient => {
                this.updatedClient = this.updatedClient;
            });
            console.log('isedit works...');
            this.showSaveAlerts = false;
            this.dontShowAlerts = true;
        } else {
            this.clientservice.save(this.client).subscribe(clientmanagement => {});
            console.log('normal save works...');
            this.showSaveAlerts = true;
            this.dontShowAlerts = false;
        }

        this.client = {
            id: 0,
            clientname: '',
            clientcode: '',
            strategy: '',
            rm: '',
            distributor: ''
        };
    }
    reset() {
        this.client = {
            id: 0,
            clientcode: '',
            clientname: '',
            strategy: '',
            rm: '',
            distributor: ''
        };
    }
    clear() {
        this.client = {
            id: 0,
            clientcode: '',
            clientname: '',
            strategy: '',
            rm: '',
            distributor: ''
        };
    }

    // findByClientcode(clientManagment) {
    //     this.clientservice.findByClientcode(this.clientmanagement).subscribe(clientDatabyCode => {
    //         this.clientDataByCode = client;

    //         console.log(this.client);
    //         this.fetchData = true;
    //     });
    // }

    //     search() {
    //         if (showByClientCode) {
    //             this.clientservice.findByClientcode(this.clientmanagement).subscribe(clientDataByCode => {
    //                 this.clientDataByCode = clientDataByCode;
    //                 console.log(this.clientDataByCode);
    //             });
    //             this.fetchData = true;
    //             this.showByClientCode = true;
    //         }
    //         else {
    //             this.clientservice.search().subscribe(clientmanagement => {
    //                 this.clientmanagement = clientmanagement;
    //                 this.fetchData = true;
    //                 this.showByClientCode = false;
    //             });
    //     }
    // }
    edit(client) {
        this.isEdit = true;
        this.client = client;
    }

    delete(id) {
        this.clientservice.deleteClient(id).subscribe(deletedItem => {
            for (let i = 0; i < this.clientmanagement.length; i++) {
                if (this.clientmanagement[i] == id) {
                    this.clientmanagement.splice(i, 1);
                }
            }
        });
    }
}
