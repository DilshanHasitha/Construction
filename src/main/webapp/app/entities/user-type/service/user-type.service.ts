import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserType, NewUserType } from '../user-type.model';

export type PartialUpdateUserType = Partial<IUserType> & Pick<IUserType, 'id'>;

export type EntityResponseType = HttpResponse<IUserType>;
export type EntityArrayResponseType = HttpResponse<IUserType[]>;

@Injectable({ providedIn: 'root' })
export class UserTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userType: NewUserType): Observable<EntityResponseType> {
    return this.http.post<IUserType>(this.resourceUrl, userType, { observe: 'response' });
  }

  update(userType: IUserType): Observable<EntityResponseType> {
    return this.http.put<IUserType>(`${this.resourceUrl}/${this.getUserTypeIdentifier(userType)}`, userType, { observe: 'response' });
  }

  partialUpdate(userType: PartialUpdateUserType): Observable<EntityResponseType> {
    return this.http.patch<IUserType>(`${this.resourceUrl}/${this.getUserTypeIdentifier(userType)}`, userType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserTypeIdentifier(userType: Pick<IUserType, 'id'>): number {
    return userType.id;
  }

  compareUserType(o1: Pick<IUserType, 'id'> | null, o2: Pick<IUserType, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserTypeIdentifier(o1) === this.getUserTypeIdentifier(o2) : o1 === o2;
  }

  addUserTypeToCollectionIfMissing<Type extends Pick<IUserType, 'id'>>(
    userTypeCollection: Type[],
    ...userTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userTypes: Type[] = userTypesToCheck.filter(isPresent);
    if (userTypes.length > 0) {
      const userTypeCollectionIdentifiers = userTypeCollection.map(userTypeItem => this.getUserTypeIdentifier(userTypeItem)!);
      const userTypesToAdd = userTypes.filter(userTypeItem => {
        const userTypeIdentifier = this.getUserTypeIdentifier(userTypeItem);
        if (userTypeCollectionIdentifiers.includes(userTypeIdentifier)) {
          return false;
        }
        userTypeCollectionIdentifiers.push(userTypeIdentifier);
        return true;
      });
      return [...userTypesToAdd, ...userTypeCollection];
    }
    return userTypeCollection;
  }
}
