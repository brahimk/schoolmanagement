import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDiscipline } from 'app/shared/model/discipline.model';

type EntityResponseType = HttpResponse<IDiscipline>;
type EntityArrayResponseType = HttpResponse<IDiscipline[]>;

@Injectable({ providedIn: 'root' })
export class DisciplineService {
  public resourceUrl = SERVER_API_URL + 'api/disciplines';

  constructor(protected http: HttpClient) {}

  create(discipline: IDiscipline): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discipline);
    return this.http
      .post<IDiscipline>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(discipline: IDiscipline): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discipline);
    return this.http
      .put<IDiscipline>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDiscipline>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDiscipline[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(discipline: IDiscipline): IDiscipline {
    const copy: IDiscipline = Object.assign({}, discipline, {
      date: discipline.date && discipline.date.isValid() ? discipline.date.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((discipline: IDiscipline) => {
        discipline.date = discipline.date ? moment(discipline.date) : undefined;
      });
    }
    return res;
  }
}
