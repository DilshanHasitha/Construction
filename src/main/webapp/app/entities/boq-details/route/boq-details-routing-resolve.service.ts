import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBOQDetails } from '../boq-details.model';
import { BOQDetailsService } from '../service/boq-details.service';

@Injectable({ providedIn: 'root' })
export class BOQDetailsRoutingResolveService implements Resolve<IBOQDetails | null> {
  constructor(protected service: BOQDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBOQDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bOQDetails: HttpResponse<IBOQDetails>) => {
          if (bOQDetails.body) {
            return of(bOQDetails.body);
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
