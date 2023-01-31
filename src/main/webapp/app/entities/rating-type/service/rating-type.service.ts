import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRatingType, NewRatingType } from '../rating-type.model';

export type PartialUpdateRatingType = Partial<IRatingType> & Pick<IRatingType, 'id'>;

export type EntityResponseType = HttpResponse<IRatingType>;
export type EntityArrayResponseType = HttpResponse<IRatingType[]>;

@Injectable({ providedIn: 'root' })
export class RatingTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rating-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ratingType: NewRatingType): Observable<EntityResponseType> {
    return this.http.post<IRatingType>(this.resourceUrl, ratingType, { observe: 'response' });
  }

  update(ratingType: IRatingType): Observable<EntityResponseType> {
    return this.http.put<IRatingType>(`${this.resourceUrl}/${this.getRatingTypeIdentifier(ratingType)}`, ratingType, {
      observe: 'response',
    });
  }

  partialUpdate(ratingType: PartialUpdateRatingType): Observable<EntityResponseType> {
    return this.http.patch<IRatingType>(`${this.resourceUrl}/${this.getRatingTypeIdentifier(ratingType)}`, ratingType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRatingType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRatingType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRatingTypeIdentifier(ratingType: Pick<IRatingType, 'id'>): number {
    return ratingType.id;
  }

  compareRatingType(o1: Pick<IRatingType, 'id'> | null, o2: Pick<IRatingType, 'id'> | null): boolean {
    return o1 && o2 ? this.getRatingTypeIdentifier(o1) === this.getRatingTypeIdentifier(o2) : o1 === o2;
  }

  addRatingTypeToCollectionIfMissing<Type extends Pick<IRatingType, 'id'>>(
    ratingTypeCollection: Type[],
    ...ratingTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ratingTypes: Type[] = ratingTypesToCheck.filter(isPresent);
    if (ratingTypes.length > 0) {
      const ratingTypeCollectionIdentifiers = ratingTypeCollection.map(ratingTypeItem => this.getRatingTypeIdentifier(ratingTypeItem)!);
      const ratingTypesToAdd = ratingTypes.filter(ratingTypeItem => {
        const ratingTypeIdentifier = this.getRatingTypeIdentifier(ratingTypeItem);
        if (ratingTypeCollectionIdentifiers.includes(ratingTypeIdentifier)) {
          return false;
        }
        ratingTypeCollectionIdentifiers.push(ratingTypeIdentifier);
        return true;
      });
      return [...ratingTypesToAdd, ...ratingTypeCollection];
    }
    return ratingTypeCollection;
  }
}
