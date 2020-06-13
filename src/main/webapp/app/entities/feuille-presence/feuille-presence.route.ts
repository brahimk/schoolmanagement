import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFeuillePresence, FeuillePresence } from 'app/shared/model/feuille-presence.model';
import { FeuillePresenceService } from './feuille-presence.service';
import { FeuillePresenceComponent } from './feuille-presence.component';
import { FeuillePresenceDetailComponent } from './feuille-presence-detail.component';
import { FeuillePresenceUpdateComponent } from './feuille-presence-update.component';

@Injectable({ providedIn: 'root' })
export class FeuillePresenceResolve implements Resolve<IFeuillePresence> {
  constructor(private service: FeuillePresenceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeuillePresence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((feuillePresence: HttpResponse<FeuillePresence>) => {
          if (feuillePresence.body) {
            return of(feuillePresence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FeuillePresence());
  }
}

export const feuillePresenceRoute: Routes = [
  {
    path: '',
    component: FeuillePresenceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FeuillePresences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FeuillePresenceDetailComponent,
    resolve: {
      feuillePresence: FeuillePresenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FeuillePresences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FeuillePresenceUpdateComponent,
    resolve: {
      feuillePresence: FeuillePresenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FeuillePresences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FeuillePresenceUpdateComponent,
    resolve: {
      feuillePresence: FeuillePresenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FeuillePresences'
    },
    canActivate: [UserRouteAccessService]
  }
];
