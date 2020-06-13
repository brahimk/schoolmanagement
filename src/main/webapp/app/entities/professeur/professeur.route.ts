import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProfesseur, Professeur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from './professeur.service';
import { ProfesseurComponent } from './professeur.component';
import { ProfesseurDetailComponent } from './professeur-detail.component';
import { ProfesseurUpdateComponent } from './professeur-update.component';

@Injectable({ providedIn: 'root' })
export class ProfesseurResolve implements Resolve<IProfesseur> {
  constructor(private service: ProfesseurService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfesseur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((professeur: HttpResponse<Professeur>) => {
          if (professeur.body) {
            return of(professeur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Professeur());
  }
}

export const professeurRoute: Routes = [
  {
    path: '',
    component: ProfesseurComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Professeurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProfesseurDetailComponent,
    resolve: {
      professeur: ProfesseurResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Professeurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProfesseurUpdateComponent,
    resolve: {
      professeur: ProfesseurResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Professeurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProfesseurUpdateComponent,
    resolve: {
      professeur: ProfesseurResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Professeurs'
    },
    canActivate: [UserRouteAccessService]
  }
];
