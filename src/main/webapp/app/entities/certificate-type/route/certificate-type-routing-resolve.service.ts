import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICertificateType } from '../certificate-type.model';
import { CertificateTypeService } from '../service/certificate-type.service';

@Injectable({ providedIn: 'root' })
export class CertificateTypeRoutingResolveService implements Resolve<ICertificateType | null> {
  constructor(protected service: CertificateTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICertificateType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((certificateType: HttpResponse<ICertificateType>) => {
          if (certificateType.body) {
            return of(certificateType.body);
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
