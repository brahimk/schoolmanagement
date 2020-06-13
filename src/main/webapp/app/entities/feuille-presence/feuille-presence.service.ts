import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFeuillePresence } from 'app/shared/model/feuille-presence.model';

type EntityResponseType = HttpResponse<IFeuillePresence>;
type EntityArrayResponseType = HttpResponse<IFeuillePresence[]>;

@Injectable({ providedIn: 'root' })
export class FeuillePresenceService {
  public resourceUrl = SERVER_API_URL + 'api/feuille-presences';

  constructor(protected http: HttpClient) {}

  create(feuillePresence: IFeuillePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feuillePresence);
    return this.http
      .post<IFeuillePresence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(feuillePresence: IFeuillePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feuillePresence);
    return this.http
      .put<IFeuillePresence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFeuillePresence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFeuillePresence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(feuillePresence: IFeuillePresence): IFeuillePresence {
    const copy: IFeuillePresence = Object.assign({}, feuillePresence, {
      date: feuillePresence.date && feuillePresence.date.isValid() ? feuillePresence.date.toJSON() : undefined
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
      res.body.forEach((feuillePresence: IFeuillePresence) => {
        feuillePresence.date = feuillePresence.date ? moment(feuillePresence.date) : undefined;
      });
    }
    return res;
  }
}
