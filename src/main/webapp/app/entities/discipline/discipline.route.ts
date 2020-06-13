import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDiscipline, Discipline } from 'app/shared/model/discipline.model';
import { DisciplineService } from './discipline.service';
import { DisciplineComponent } from './discipline.component';
import { DisciplineDetailComponent } from './discipline-detail.component';
import { DisciplineUpdateComponent } from './discipline-update.component';

@Injectable({ providedIn: 'root' })
export class DisciplineResolve implements Resolve<IDiscipline> {
  constructor(private service: DisciplineService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDiscipline> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((discipline: HttpResponse<Discipline>) => {
          if (discipline.body) {
            return of(discipline.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Discipline());
  }
}

export const disciplineRoute: Routes = [
  {
    path: '',
    component: DisciplineComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Disciplines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DisciplineDetailComponent,
    resolve: {
      discipline: DisciplineResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Disciplines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DisciplineUpdateComponent,
    resolve: {
      discipline: DisciplineResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Disciplines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DisciplineUpdateComponent,
    resolve: {
      discipline: DisciplineResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Disciplines'
    },
    canActivate: [UserRouteAccessService]
  }
];
