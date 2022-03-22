import { Injectable, EventEmitter, Output } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AccountService } from './account.service';
import { JhiTrackerService } from '../tracker/tracker.service';
import * as _ from 'lodash';

@Injectable({ providedIn: 'root' })
export class Principal {
    private userIdentity: any;
    private authenticated = false;
    @Output() accountValue = new EventEmitter();

    private authenticationState = new Subject<any>();
    constructor(private account: AccountService, private trackerService: JhiTrackerService) {}

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authorities: string[]): Promise<boolean> {
        return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
    }

    hasAnyAuthorityDirect(authorities: string[]): boolean {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
            return false;
        }

        for (let i = 0; i < authorities.length; i++) {
            if (this.userIdentity.authorities.includes(authorities[i])) {
                return true;
            }
        }

        return false;
    }

    hasAuthority(authority: string): Promise<boolean> {
        if (!this.authenticated) {
            return Promise.resolve(false);
        }

        return this.identity().then(
            id => {
                return Promise.resolve(id.authorities && id.authorities.includes(authority));
            },
            () => {
                return Promise.resolve(false);
            }
        );
    }

    hasAnyRole(canEnable: any) {
        return Promise.resolve(this.hasAnyRoleDirect(canEnable));
    }

    hasAnyRoleDirect(canEnable: any): boolean {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.role || !this.userIdentity.role.roleNameMasters) {
            return false;
        }
        for (let i = 0; i < this.userIdentity.role.roleNameMasters.length; i++) {
            const role = this.userIdentity.role.roleNameMasters[i];
            if (role.roleName === canEnable.roleName) {
                for (const obj in role) {
                    if (role.hasOwnProperty(obj)) {
                        if (obj === canEnable.enableFor && role[canEnable.enableFor]) {
                            return true;
                        }
                    }
                }
            }
        }
        /* for (let i = 0; i < roles.length; i++) {
            for(let k=0; k < this.userIdentity.role.roleNameMasters.length; k++) {
                const index =  _.isEqual(this.userIdentity.role.roleNameMasters[i], roles[i]);
                console.log(index, 'few');
                if (index) {
                    return true;
                }
            }
        } */
        console.log('last return false');
        return false;
    }

    identity(force?: boolean): Promise<any> {
        this.accountValue.emit(this.account);

        if (force === true) {
            this.userIdentity = undefined;
        }
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }
        return this.account
            .get()
            .toPromise()
            .then(response => {
                const account = response.body;
                if (account) {
                    this.userIdentity = account;
                    this.authenticated = true;
                    this.trackerService.connect();
                } else {
                    this.userIdentity = null;
                    this.authenticated = false;
                }
                this.authenticationState.next(this.userIdentity);
                return this.userIdentity;
            })
            .catch(err => {
                if (this.trackerService.stompClient && this.trackerService.stompClient.connected) {
                    this.trackerService.disconnect();
                }
                this.userIdentity = null;
                this.authenticated = false;
                this.authenticationState.next(this.userIdentity);
                return null;
            });
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    getImageUrl(): string {
        return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
    }
}
