import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMasterItem } from '../master-item.model';
import { MasterItemService } from '../service/master-item.service';

@Injectable({ providedIn: 'root' })
export class MasterItemRoutingResolveService implements Resolve<IMasterItem | null> {
  constructor(protected service: MasterItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMasterItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((masterItem: HttpResponse<IMasterItem>) => {
          if (masterItem.body) {
            return of(masterItem.body);
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
