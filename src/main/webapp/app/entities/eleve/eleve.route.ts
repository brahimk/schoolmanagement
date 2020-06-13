import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEleve, Eleve } from 'app/shared/model/eleve.model';
import { EleveService } from './eleve.service';
import { EleveComponent } from './eleve.component';
import { EleveDetailComponent } from './eleve-detail.component';
import { EleveUpdateComponent } from './eleve-update.component';

@Injectable({ providedIn: 'root' })
export class EleveResolve implements Resolve<IEleve> {
  constructor(private service: EleveService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEleve> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((eleve: HttpResponse<Eleve>) => {
          if (eleve.body) {
            return of(eleve.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Eleve());
  }
}

export const eleveRoute: Routes = [
  {
    path: '',
    component: EleveComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Eleves'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EleveDetailComponent,
    resolve: {
      eleve: EleveResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Eleves'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EleveUpdateComponent,
    resolve: {
      eleve: EleveResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Eleves'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EleveUpdateComponent,
    resolve: {
      eleve: EleveResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Eleves'
    },
    canActivate: [UserRouteAccessService]
  }
];
