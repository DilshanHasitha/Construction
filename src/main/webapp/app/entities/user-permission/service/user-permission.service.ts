import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserPermission, NewUserPermission } from '../user-permission.model';

export type PartialUpdateUserPermission = Partial<IUserPermission> & Pick<IUserPermission, 'id'>;

export type EntityResponseType = HttpResponse<IUserPermission>;
export type EntityArrayResponseType = HttpResponse<IUserPermission[]>;

@Injectable({ providedIn: 'root' })
export class UserPermissionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userPermission: NewUserPermission): Observable<EntityResponseType> {
    return this.http.post<IUserPermission>(this.resourceUrl, userPermission, { observe: 'response' });
  }

  update(userPermission: IUserPermission): Observable<EntityResponseType> {
    return this.http.put<IUserPermission>(`${this.resourceUrl}/${this.getUserPermissionIdentifier(userPermission)}`, userPermission, {
      observe: 'response',
    });
  }

  partialUpdate(userPermission: PartialUpdateUserPermission): Observable<EntityResponseType> {
    return this.http.patch<IUserPermission>(`${this.resourceUrl}/${this.getUserPermissionIdentifier(userPermission)}`, userPermission, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserPermission[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserPermissionIdentifier(userPermission: Pick<IUserPermission, 'id'>): number {
    return userPermission.id;
  }

  compareUserPermission(o1: Pick<IUserPermission, 'id'> | null, o2: Pick<IUserPermission, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserPermissionIdentifier(o1) === this.getUserPermissionIdentifier(o2) : o1 === o2;
  }

  addUserPermissionToCollectionIfMissing<Type extends Pick<IUserPermission, 'id'>>(
    userPermissionCollection: Type[],
    ...userPermissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userPermissions: Type[] = userPermissionsToCheck.filter(isPresent);
    if (userPermissions.length > 0) {
      const userPermissionCollectionIdentifiers = userPermissionCollection.map(
        userPermissionItem => this.getUserPermissionIdentifier(userPermissionItem)!
      );
      const userPermissionsToAdd = userPermissions.filter(userPermissionItem => {
        const userPermissionIdentifier = this.getUserPermissionIdentifier(userPermissionItem);
        if (userPermissionCollectionIdentifiers.includes(userPermissionIdentifier)) {
          return false;
        }
        userPermissionCollectionIdentifiers.push(userPermissionIdentifier);
        return true;
      });
      return [...userPermissionsToAdd, ...userPermissionCollection];
    }
    return userPermissionCollection;
  }
}
