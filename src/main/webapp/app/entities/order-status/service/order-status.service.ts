import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderStatus, NewOrderStatus } from '../order-status.model';

export type PartialUpdateOrderStatus = Partial<IOrderStatus> & Pick<IOrderStatus, 'id'>;

export type EntityResponseType = HttpResponse<IOrderStatus>;
export type EntityArrayResponseType = HttpResponse<IOrderStatus[]>;

@Injectable({ providedIn: 'root' })
export class OrderStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderStatus: NewOrderStatus): Observable<EntityResponseType> {
    return this.http.post<IOrderStatus>(this.resourceUrl, orderStatus, { observe: 'response' });
  }

  update(orderStatus: IOrderStatus): Observable<EntityResponseType> {
    return this.http.put<IOrderStatus>(`${this.resourceUrl}/${this.getOrderStatusIdentifier(orderStatus)}`, orderStatus, {
      observe: 'response',
    });
  }

  partialUpdate(orderStatus: PartialUpdateOrderStatus): Observable<EntityResponseType> {
    return this.http.patch<IOrderStatus>(`${this.resourceUrl}/${this.getOrderStatusIdentifier(orderStatus)}`, orderStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderStatusIdentifier(orderStatus: Pick<IOrderStatus, 'id'>): number {
    return orderStatus.id;
  }

  compareOrderStatus(o1: Pick<IOrderStatus, 'id'> | null, o2: Pick<IOrderStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderStatusIdentifier(o1) === this.getOrderStatusIdentifier(o2) : o1 === o2;
  }

  addOrderStatusToCollectionIfMissing<Type extends Pick<IOrderStatus, 'id'>>(
    orderStatusCollection: Type[],
    ...orderStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderStatuses: Type[] = orderStatusesToCheck.filter(isPresent);
    if (orderStatuses.length > 0) {
      const orderStatusCollectionIdentifiers = orderStatusCollection.map(
        orderStatusItem => this.getOrderStatusIdentifier(orderStatusItem)!
      );
      const orderStatusesToAdd = orderStatuses.filter(orderStatusItem => {
        const orderStatusIdentifier = this.getOrderStatusIdentifier(orderStatusItem);
        if (orderStatusCollectionIdentifiers.includes(orderStatusIdentifier)) {
          return false;
        }
        orderStatusCollectionIdentifiers.push(orderStatusIdentifier);
        return true;
      });
      return [...orderStatusesToAdd, ...orderStatusCollection];
    }
    return orderStatusCollection;
  }
}
