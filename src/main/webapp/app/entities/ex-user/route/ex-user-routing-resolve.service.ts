import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExUser } from '../ex-user.model';
import { ExUserService } from '../service/ex-user.service';

@Injectable({ providedIn: 'root' })
export class ExUserRoutingResolveService implements Resolve<IExUser | null> {
  constructor(protected service: ExUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exUser: HttpResponse<IExUser>) => {
          if (exUser.body) {
            return of(exUser.body);
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
