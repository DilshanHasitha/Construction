import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExUserComponent } from '../list/ex-user.component';
import { ExUserDetailComponent } from '../detail/ex-user-detail.component';
import { ExUserUpdateComponent } from '../update/ex-user-update.component';
import { ExUserRoutingResolveService } from './ex-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const exUserRoute: Routes = [
  {
    path: '',
    component: ExUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExUserDetailComponent,
    resolve: {
      exUser: ExUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exUserRoute)],
  exports: [RouterModule],
})
export class ExUserRoutingModule {}
