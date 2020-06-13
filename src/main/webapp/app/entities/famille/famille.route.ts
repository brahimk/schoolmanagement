import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFamille, Famille } from 'app/shared/model/famille.model';
import { FamilleService } from './famille.service';
import { FamilleComponent } from './famille.component';
import { FamilleDetailComponent } from './famille-detail.component';
import { FamilleUpdateComponent } from './famille-update.component';

@Injectable({ providedIn: 'root' })
export class FamilleResolve implements Resolve<IFamille> {
  constructor(private service: FamilleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamille> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((famille: HttpResponse<Famille>) => {
          if (famille.body) {
            return of(famille.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Famille());
  }
}

export const familleRoute: Routes = [
  {
    path: '',
    component: FamilleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Familles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FamilleDetailComponent,
    resolve: {
      famille: FamilleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Familles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FamilleUpdateComponent,
    resolve: {
      famille: FamilleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Familles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FamilleUpdateComponent,
    resolve: {
      famille: FamilleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Familles'
    },
    canActivate: [UserRouteAccessService]
  }
];
