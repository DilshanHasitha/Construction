import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBOQDetails, NewBOQDetails } from '../boq-details.model';

export type PartialUpdateBOQDetails = Partial<IBOQDetails> & Pick<IBOQDetails, 'id'>;

type RestOf<T extends IBOQDetails | NewBOQDetails> = Omit<T, 'orderPlacedOn'> & {
  orderPlacedOn?: string | null;
};

export type RestBOQDetails = RestOf<IBOQDetails>;

export type NewRestBOQDetails = RestOf<NewBOQDetails>;

export type PartialUpdateRestBOQDetails = RestOf<PartialUpdateBOQDetails>;

export type EntityResponseType = HttpResponse<IBOQDetails>;
export type EntityArrayResponseType = HttpResponse<IBOQDetails[]>;

@Injectable({ providedIn: 'root' })
export class BOQDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/boq-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bOQDetails: NewBOQDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bOQDetails);
    return this.http
      .post<RestBOQDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bOQDetails: IBOQDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bOQDetails);
    return this.http
      .put<RestBOQDetails>(`${this.resourceUrl}/${this.getBOQDetailsIdentifier(bOQDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bOQDetails: PartialUpdateBOQDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bOQDetails);
    return this.http
      .patch<RestBOQDetails>(`${this.resourceUrl}/${this.getBOQDetailsIdentifier(bOQDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBOQDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBOQDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBOQDetailsIdentifier(bOQDetails: Pick<IBOQDetails, 'id'>): number {
    return bOQDetails.id;
  }

  compareBOQDetails(o1: Pick<IBOQDetails, 'id'> | null, o2: Pick<IBOQDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getBOQDetailsIdentifier(o1) === this.getBOQDetailsIdentifier(o2) : o1 === o2;
  }

  addBOQDetailsToCollectionIfMissing<Type extends Pick<IBOQDetails, 'id'>>(
    bOQDetailsCollection: Type[],
    ...bOQDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bOQDetails: Type[] = bOQDetailsToCheck.filter(isPresent);
    if (bOQDetails.length > 0) {
      const bOQDetailsCollectionIdentifiers = bOQDetailsCollection.map(bOQDetailsItem => this.getBOQDetailsIdentifier(bOQDetailsItem)!);
      const bOQDetailsToAdd = bOQDetails.filter(bOQDetailsItem => {
        const bOQDetailsIdentifier = this.getBOQDetailsIdentifier(bOQDetailsItem);
        if (bOQDetailsCollectionIdentifiers.includes(bOQDetailsIdentifier)) {
          return false;
        }
        bOQDetailsCollectionIdentifiers.push(bOQDetailsIdentifier);
        return true;
      });
      return [...bOQDetailsToAdd, ...bOQDetailsCollection];
    }
    return bOQDetailsCollection;
  }

  protected convertDateFromClient<T extends IBOQDetails | NewBOQDetails | PartialUpdateBOQDetails>(bOQDetails: T): RestOf<T> {
    return {
      ...bOQDetails,
      orderPlacedOn: bOQDetails.orderPlacedOn?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBOQDetails: RestBOQDetails): IBOQDetails {
    return {
      ...restBOQDetails,
      orderPlacedOn: restBOQDetails.orderPlacedOn ? dayjs(restBOQDetails.orderPlacedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBOQDetails>): HttpResponse<IBOQDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBOQDetails[]>): HttpResponse<IBOQDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
