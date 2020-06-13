import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClasse, Classe } from 'app/shared/model/classe.model';
import { ClasseService } from './classe.service';
import { ClasseComponent } from './classe.component';
import { ClasseDetailComponent } from './classe-detail.component';
import { ClasseUpdateComponent } from './classe-update.component';

@Injectable({ providedIn: 'root' })
export class ClasseResolve implements Resolve<IClasse> {
  constructor(private service: ClasseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClasse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((classe: HttpResponse<Classe>) => {
          if (classe.body) {
            return of(classe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Classe());
  }
}

export const classeRoute: Routes = [
  {
    path: '',
    component: ClasseComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Classes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClasseDetailComponent,
    resolve: {
      classe: ClasseResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Classes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClasseUpdateComponent,
    resolve: {
      classe: ClasseResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Classes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClasseUpdateComponent,
    resolve: {
      classe: ClasseResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Classes'
    },
    canActivate: [UserRouteAccessService]
  }
];
