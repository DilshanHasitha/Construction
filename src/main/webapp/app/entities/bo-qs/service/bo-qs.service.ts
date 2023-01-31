import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBOQs, NewBOQs } from '../bo-qs.model';

export type PartialUpdateBOQs = Partial<IBOQs> & Pick<IBOQs, 'id'>;

export type EntityResponseType = HttpResponse<IBOQs>;
export type EntityArrayResponseType = HttpResponse<IBOQs[]>;

@Injectable({ providedIn: 'root' })
export class BOQsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bo-qs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bOQs: NewBOQs): Observable<EntityResponseType> {
    return this.http.post<IBOQs>(this.resourceUrl, bOQs, { observe: 'response' });
  }

  update(bOQs: IBOQs): Observable<EntityResponseType> {
    return this.http.put<IBOQs>(`${this.resourceUrl}/${this.getBOQsIdentifier(bOQs)}`, bOQs, { observe: 'response' });
  }

  partialUpdate(bOQs: PartialUpdateBOQs): Observable<EntityResponseType> {
    return this.http.patch<IBOQs>(`${this.resourceUrl}/${this.getBOQsIdentifier(bOQs)}`, bOQs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBOQs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBOQs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBOQsIdentifier(bOQs: Pick<IBOQs, 'id'>): number {
    return bOQs.id;
  }

  compareBOQs(o1: Pick<IBOQs, 'id'> | null, o2: Pick<IBOQs, 'id'> | null): boolean {
    return o1 && o2 ? this.getBOQsIdentifier(o1) === this.getBOQsIdentifier(o2) : o1 === o2;
  }

  addBOQsToCollectionIfMissing<Type extends Pick<IBOQs, 'id'>>(
    bOQsCollection: Type[],
    ...bOQsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bOQs: Type[] = bOQsToCheck.filter(isPresent);
    if (bOQs.length > 0) {
      const bOQsCollectionIdentifiers = bOQsCollection.map(bOQsItem => this.getBOQsIdentifier(bOQsItem)!);
      const bOQsToAdd = bOQs.filter(bOQsItem => {
        const bOQsIdentifier = this.getBOQsIdentifier(bOQsItem);
        if (bOQsCollectionIdentifiers.includes(bOQsIdentifier)) {
          return false;
        }
        bOQsCollectionIdentifiers.push(bOQsIdentifier);
        return true;
      });
      return [...bOQsToAdd, ...bOQsCollection];
    }
    return bOQsCollection;
  }
}
