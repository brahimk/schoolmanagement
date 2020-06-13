import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEleve } from 'app/shared/model/eleve.model';

type EntityResponseType = HttpResponse<IEleve>;
type EntityArrayResponseType = HttpResponse<IEleve[]>;

@Injectable({ providedIn: 'root' })
export class EleveService {
  public resourceUrl = SERVER_API_URL + 'api/eleves';

  constructor(protected http: HttpClient) {}

  create(eleve: IEleve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eleve);
    return this.http
      .post<IEleve>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(eleve: IEleve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eleve);
    return this.http
      .put<IEleve>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEleve>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEleve[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(eleve: IEleve): IEleve {
    const copy: IEleve = Object.assign({}, eleve, {
      dateNaissance: eleve.dateNaissance && eleve.dateNaissance.isValid() ? eleve.dateNaissance.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? moment(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((eleve: IEleve) => {
        eleve.dateNaissance = eleve.dateNaissance ? moment(eleve.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
