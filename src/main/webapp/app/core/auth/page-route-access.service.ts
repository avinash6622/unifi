import { Injectable, isDevMode } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { Principal } from '../';
import { StateStorageService } from './state-storage.service';

@Injectable({ providedIn: 'root' })
export class PageRouteAccessService implements CanActivate {
    constructor(private router: Router, private principal: Principal, private stateStorageService: StateStorageService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {
        const canEnable = route.data['canEnable'];
        console.log(route.data['canEnable']);
        return this.checkLogin(state.url, canEnable);
    }

    checkLogin(url: string, canEnable: any): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(
            principal.identity().then(account => {
                if (!canEnable || canEnable.length === 0) {
                    return true;
                }

                if (account) {
                    return principal.hasAnyRole(canEnable).then(response => {
                        console.log(response);
                        if (response) {
                            return true;
                        }
                        if (isDevMode()) {
                            console.error('User has not any of required roles: ', canEnable);
                        }
                        return false;
                    });
                }

                this.stateStorageService.storeUrl(url);
                this.router.navigate(['accessdenied']).then(() => {
                    if (!account) {
                        console.log('Checking...');
                    }
                });
                return false;
            })
        );
    }
}
