import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProfesseur } from 'app/shared/model/professeur.model';

type EntityResponseType = HttpResponse<IProfesseur>;
type EntityArrayResponseType = HttpResponse<IProfesseur[]>;

@Injectable({ providedIn: 'root' })
export class ProfesseurService {
  public resourceUrl = SERVER_API_URL + 'api/professeurs';

  constructor(protected http: HttpClient) {}

  create(professeur: IProfesseur): Observable<EntityResponseType> {
    return this.http.post<IProfesseur>(this.resourceUrl, professeur, { observe: 'response' });
  }

  update(professeur: IProfesseur): Observable<EntityResponseType> {
    return this.http.put<IProfesseur>(this.resourceUrl, professeur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfesseur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfesseur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
