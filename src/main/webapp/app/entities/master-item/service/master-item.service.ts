import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMasterItem, NewMasterItem } from '../master-item.model';

export type PartialUpdateMasterItem = Partial<IMasterItem> & Pick<IMasterItem, 'id'>;

export type EntityResponseType = HttpResponse<IMasterItem>;
export type EntityArrayResponseType = HttpResponse<IMasterItem[]>;

@Injectable({ providedIn: 'root' })
export class MasterItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/master-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(masterItem: NewMasterItem): Observable<EntityResponseType> {
    return this.http.post<IMasterItem>(this.resourceUrl, masterItem, { observe: 'response' });
  }

  update(masterItem: IMasterItem): Observable<EntityResponseType> {
    return this.http.put<IMasterItem>(`${this.resourceUrl}/${this.getMasterItemIdentifier(masterItem)}`, masterItem, {
      observe: 'response',
    });
  }

  partialUpdate(masterItem: PartialUpdateMasterItem): Observable<EntityResponseType> {
    return this.http.patch<IMasterItem>(`${this.resourceUrl}/${this.getMasterItemIdentifier(masterItem)}`, masterItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMasterItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMasterItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMasterItemIdentifier(masterItem: Pick<IMasterItem, 'id'>): number {
    return masterItem.id;
  }

  compareMasterItem(o1: Pick<IMasterItem, 'id'> | null, o2: Pick<IMasterItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getMasterItemIdentifier(o1) === this.getMasterItemIdentifier(o2) : o1 === o2;
  }

  addMasterItemToCollectionIfMissing<Type extends Pick<IMasterItem, 'id'>>(
    masterItemCollection: Type[],
    ...masterItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const masterItems: Type[] = masterItemsToCheck.filter(isPresent);
    if (masterItems.length > 0) {
      const masterItemCollectionIdentifiers = masterItemCollection.map(masterItemItem => this.getMasterItemIdentifier(masterItemItem)!);
      const masterItemsToAdd = masterItems.filter(masterItemItem => {
        const masterItemIdentifier = this.getMasterItemIdentifier(masterItemItem);
        if (masterItemCollectionIdentifiers.includes(masterItemIdentifier)) {
          return false;
        }
        masterItemCollectionIdentifiers.push(masterItemIdentifier);
        return true;
      });
      return [...masterItemsToAdd, ...masterItemCollection];
    }
    return masterItemCollection;
  }
}
