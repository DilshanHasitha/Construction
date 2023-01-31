import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRatingType } from '../rating-type.model';
import { RatingTypeService } from '../service/rating-type.service';

@Injectable({ providedIn: 'root' })
export class RatingTypeRoutingResolveService implements Resolve<IRatingType | null> {
  constructor(protected service: RatingTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRatingType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ratingType: HttpResponse<IRatingType>) => {
          if (ratingType.body) {
            return of(ratingType.body);
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
