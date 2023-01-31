import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserPermission } from '../user-permission.model';
import { UserPermissionService } from '../service/user-permission.service';

@Injectable({ providedIn: 'root' })
export class UserPermissionRoutingResolveService implements Resolve<IUserPermission | null> {
  constructor(protected service: UserPermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserPermission | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userPermission: HttpResponse<IUserPermission>) => {
          if (userPermission.body) {
            return of(userPermission.body);
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
