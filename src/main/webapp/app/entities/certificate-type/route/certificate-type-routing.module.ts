import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CertificateTypeComponent } from '../list/certificate-type.component';
import { CertificateTypeDetailComponent } from '../detail/certificate-type-detail.component';
import { CertificateTypeUpdateComponent } from '../update/certificate-type-update.component';
import { CertificateTypeRoutingResolveService } from './certificate-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const certificateTypeRoute: Routes = [
  {
    path: '',
    component: CertificateTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CertificateTypeDetailComponent,
    resolve: {
      certificateType: CertificateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CertificateTypeUpdateComponent,
    resolve: {
      certificateType: CertificateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CertificateTypeUpdateComponent,
    resolve: {
      certificateType: CertificateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(certificateTypeRoute)],
  exports: [RouterModule],
})
export class CertificateTypeRoutingModule {}
