import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUnitOfMeasure, NewUnitOfMeasure } from '../unit-of-measure.model';

export type PartialUpdateUnitOfMeasure = Partial<IUnitOfMeasure> & Pick<IUnitOfMeasure, 'id'>;

export type EntityResponseType = HttpResponse<IUnitOfMeasure>;
export type EntityArrayResponseType = HttpResponse<IUnitOfMeasure[]>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/unit-of-measures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(unitOfMeasure: NewUnitOfMeasure): Observable<EntityResponseType> {
    return this.http.post<IUnitOfMeasure>(this.resourceUrl, unitOfMeasure, { observe: 'response' });
  }

  update(unitOfMeasure: IUnitOfMeasure): Observable<EntityResponseType> {
    return this.http.put<IUnitOfMeasure>(`${this.resourceUrl}/${this.getUnitOfMeasureIdentifier(unitOfMeasure)}`, unitOfMeasure, {
      observe: 'response',
    });
  }

  partialUpdate(unitOfMeasure: PartialUpdateUnitOfMeasure): Observable<EntityResponseType> {
    return this.http.patch<IUnitOfMeasure>(`${this.resourceUrl}/${this.getUnitOfMeasureIdentifier(unitOfMeasure)}`, unitOfMeasure, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUnitOfMeasure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnitOfMeasure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUnitOfMeasureIdentifier(unitOfMeasure: Pick<IUnitOfMeasure, 'id'>): number {
    return unitOfMeasure.id;
  }

  compareUnitOfMeasure(o1: Pick<IUnitOfMeasure, 'id'> | null, o2: Pick<IUnitOfMeasure, 'id'> | null): boolean {
    return o1 && o2 ? this.getUnitOfMeasureIdentifier(o1) === this.getUnitOfMeasureIdentifier(o2) : o1 === o2;
  }

  addUnitOfMeasureToCollectionIfMissing<Type extends Pick<IUnitOfMeasure, 'id'>>(
    unitOfMeasureCollection: Type[],
    ...unitOfMeasuresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const unitOfMeasures: Type[] = unitOfMeasuresToCheck.filter(isPresent);
    if (unitOfMeasures.length > 0) {
      const unitOfMeasureCollectionIdentifiers = unitOfMeasureCollection.map(
        unitOfMeasureItem => this.getUnitOfMeasureIdentifier(unitOfMeasureItem)!
      );
      const unitOfMeasuresToAdd = unitOfMeasures.filter(unitOfMeasureItem => {
        const unitOfMeasureIdentifier = this.getUnitOfMeasureIdentifier(unitOfMeasureItem);
        if (unitOfMeasureCollectionIdentifiers.includes(unitOfMeasureIdentifier)) {
          return false;
        }
        unitOfMeasureCollectionIdentifiers.push(unitOfMeasureIdentifier);
        return true;
      });
      return [...unitOfMeasuresToAdd, ...unitOfMeasureCollection];
    }
    return unitOfMeasureCollection;
  }
}
