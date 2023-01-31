import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBOQs } from '../bo-qs.model';
import { BOQsService } from '../service/bo-qs.service';

@Injectable({ providedIn: 'root' })
export class BOQsRoutingResolveService implements Resolve<IBOQs | null> {
  constructor(protected service: BOQsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBOQs | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bOQs: HttpResponse<IBOQs>) => {
          if (bOQs.body) {
            return of(bOQs.body);
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
