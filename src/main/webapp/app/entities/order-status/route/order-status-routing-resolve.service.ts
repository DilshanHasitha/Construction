import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderStatus } from '../order-status.model';
import { OrderStatusService } from '../service/order-status.service';

@Injectable({ providedIn: 'root' })
export class OrderStatusRoutingResolveService implements Resolve<IOrderStatus | null> {
  constructor(protected service: OrderStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderStatus | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderStatus: HttpResponse<IOrderStatus>) => {
          if (orderStatus.body) {
            return of(orderStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
