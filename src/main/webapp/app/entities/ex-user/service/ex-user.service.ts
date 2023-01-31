import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExUser, NewExUser } from '../ex-user.model';

export type PartialUpdateExUser = Partial<IExUser> & Pick<IExUser, 'id'>;

export type EntityResponseType = HttpResponse<IExUser>;
export type EntityArrayResponseType = HttpResponse<IExUser[]>;

@Injectable({ providedIn: 'root' })
export class ExUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ex-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exUser: NewExUser): Observable<EntityResponseType> {
    return this.http.post<IExUser>(this.resourceUrl, exUser, { observe: 'response' });
  }

  update(exUser: IExUser): Observable<EntityResponseType> {
    return this.http.put<IExUser>(`${this.resourceUrl}/${this.getExUserIdentifier(exUser)}`, exUser, { observe: 'response' });
  }

  partialUpdate(exUser: PartialUpdateExUser): Observable<EntityResponseType> {
    return this.http.patch<IExUser>(`${this.resourceUrl}/${this.getExUserIdentifier(exUser)}`, exUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExUserIdentifier(exUser: Pick<IExUser, 'id'>): number {
    return exUser.id;
  }

  compareExUser(o1: Pick<IExUser, 'id'> | null, o2: Pick<IExUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getExUserIdentifier(o1) === this.getExUserIdentifier(o2) : o1 === o2;
  }

  addExUserToCollectionIfMissing<Type extends Pick<IExUser, 'id'>>(
    exUserCollection: Type[],
    ...exUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exUsers: Type[] = exUsersToCheck.filter(isPresent);
    if (exUsers.length > 0) {
      const exUserCollectionIdentifiers = exUserCollection.map(exUserItem => this.getExUserIdentifier(exUserItem)!);
      const exUsersToAdd = exUsers.filter(exUserItem => {
        const exUserIdentifier = this.getExUserIdentifier(exUserItem);
        if (exUserCollectionIdentifiers.includes(exUserIdentifier)) {
          return false;
        }
        exUserCollectionIdentifiers.push(exUserIdentifier);
        return true;
      });
      return [...exUsersToAdd, ...exUserCollection];
    }
    return exUserCollection;
  }
}
