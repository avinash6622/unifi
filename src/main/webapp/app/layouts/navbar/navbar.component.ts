import { Component, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { VERSION } from 'app/app.constants';
import { Principal, LoginService } from 'app/core';
import { ProfileService } from '../profiles/profile.service';
import { Account } from '../../core/user/account.model';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.css']
})
export class NavbarComponent implements OnInit {
    account: Account;
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    isShowDashboard: boolean = true;
    sub: any;

    constructor(
        private loginService: LoginService,
        private principal: Principal,
        private profileService: ProfileService,
        private router: Router,
        private route: ActivatedRoute
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.sub = this.principal;
    }

    // ngDoCheck() {
    //     // console.log(this.account);
    // }

    collapseNavbar() {
        this.isNavbarCollapsed = !this.isShowDashboard;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        console.log('Checking...');
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
    ShowDashboard() {
        this.isShowDashboard = !this.isShowDashboard;
    }
}
