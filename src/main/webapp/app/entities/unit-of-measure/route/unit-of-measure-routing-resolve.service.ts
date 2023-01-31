import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUnitOfMeasure } from '../unit-of-measure.model';
import { UnitOfMeasureService } from '../service/unit-of-measure.service';

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureRoutingResolveService implements Resolve<IUnitOfMeasure | null> {
  constructor(protected service: UnitOfMeasureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUnitOfMeasure | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((unitOfMeasure: HttpResponse<IUnitOfMeasure>) => {
          if (unitOfMeasure.body) {
            return of(unitOfMeasure.body);
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
